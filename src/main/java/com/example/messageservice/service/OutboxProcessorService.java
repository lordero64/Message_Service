package com.example.messageservice.service;


import com.example.messageservice.dto.DQLMessageDto;
import com.example.messageservice.entity.OutboxMessage;
import com.example.messageservice.repository.OutboxRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.time.LocalDateTime;

@Service
public class OutboxProcessorService {
    private static final Logger logger = LoggerFactory.getLogger(OutboxProcessorService.class);

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${app.kafka.topic.messages:messages-topic}")
    private String messagesTopic;

    @Value("${app.kafka.topic.dlx:messages-dlx}")
    private String dlxTopic;

    @Scheduled(fixedDelayString = "${app.outbox.processor.interval}")
    @Transactional
    public void processOutboxMessages() {
        List<OutboxMessage> pendingMessages = outboxRepository.findByStatusOrderByCreatedAtAsc(OutboxMessage.OutboxStatus.PENDING);

        if (pendingMessages.isEmpty()) {
            logger.debug("Нет pending сообщений для обработки");
            return;
        }

        logger.info("Найдено {} pending сообщений для обработки", pendingMessages.size());

        for (OutboxMessage outboxMessage : pendingMessages) {
            try {
                processSingleMessage(outboxMessage);
            } catch (Exception e) {
                logger.error("Ошибка обработки outbox сообщения ID: {}", outboxMessage.getId(), e);
                // Сообщение останется в статусе PENDING для повторной попытки
            }
        }
    }

    private void processSingleMessage(OutboxMessage outboxMessage) {
        try {
            // Отправляем в Kafka
            kafkaTemplate.send(messagesTopic, outboxMessage.getPayload())
                    .whenComplete((result, ex) -> {
                        if (ex == null) {
                            // Успешная отправка - отмечаем как обработанное
                            outboxMessage.markProcessed();
                            outboxRepository.save(outboxMessage);
                            logger.info("Outbox сообщение ID: {} успешно отправлено в Kafka", outboxMessage.getId());
                        } else {
                            logger.error("Ошибка отправки outbox сообщения ID: {} в Kafka", outboxMessage.getId(), ex);
                        }
                    });

        } catch (Exception e) {
            logger.error("Исключение при обработке outbox сообщения ID: {}", outboxMessage.getId(), e);
            throw e;
        }
    }

    /**
     * Очистка старых обработанных сообщений
     */
    @Scheduled(cron = "${app.outbox.cleanup.cron:0 0 2 * * ?}") // Каждый день в 2:00
    @Transactional
    public void cleanupOldMessages() {
        LocalDateTime threshold = LocalDateTime.now().minusDays(7); // Удаляем старше 7 дней
        outboxRepository.deleteOldProcessedMessages(threshold);
        logger.info("Очистка старых обработанных outbox сообщений завершена");
    }

    /**
     * Обработка зависших сообщений
     */
    @Scheduled(fixedDelayString = "${app.outbox.stuck-messages.interval:300000}") // Каждые 5 минут
    @Transactional
    public void processStuckMessages() {
        LocalDateTime threshold = LocalDateTime.now().minusMinutes(30); // Сообщения старше 30 минут
        List<OutboxMessage> stuckMessages = outboxRepository.findPendingMessagesOlderThan(threshold);

        for (OutboxMessage message : stuckMessages) {
            try {
                // Пытаемся отправить в DLQ
                String dlqPayload = createDlqPayload(message);
                kafkaTemplate.send(dlxTopic, dlqPayload);

                // Помечаем как неудачное
                message.markFailed();
                outboxRepository.save(message);
                logger.warn("Зависшее outbox сообщение ID: {} перемещено в DLQ", message.getId());

            } catch (Exception e) {
                logger.error("Ошибка обработки зависшего сообщения ID: {}", message.getId(), e);
            }
        }
    }

    private String createDlqPayload(OutboxMessage message) throws JsonProcessingException {
        DQLMessageDto dlqMessage = new DQLMessageDto(
                message.getId(),
                message.getAggregateId(),
                message.getEventType(),
                message.getPayload(),
                message.getCreatedAt(),
                "STUCK_MESSAGE"
        );
        return objectMapper.writeValueAsString(dlqMessage);
    }

}
