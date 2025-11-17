package com.example.messageservice.service;

import com.example.messageservice.dto.KafkaMessageDto;
import com.example.messageservice.dto.MessageDto;
import com.example.messageservice.entity.Message;
import com.example.messageservice.entity.OutboxMessage;
import com.example.messageservice.repository.MessageRepository;
import com.example.messageservice.repository.OutboxRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    //Не используется из-за паттерна outbox
//    @Autowired
//    private KafkaMessageService kafkaMessageService;

    @Autowired
    private OutboxRepository outboxRepository;

    @Autowired
    private ObjectMapper objectMapper;
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Transactional
    public Message createMessage(MessageDto messageDto){
        logger.info("Создание сообщения: {}", messageDto.getContent());

        Message message = new Message(messageDto.getContent());
        Message savedMessage = messageRepository.save(message);

        logger.info("Сообщение сохранено в БД с ID: {}", savedMessage.getId());

        // Простое сохранение в Kafka
//        try {
//            KafkaMessageDto kafkaMessage = new KafkaMessageDto(savedMessage.getId(),
//                    savedMessage.getContent(),
//                    savedMessage.getCreatedAt());
//
//            kafkaMessageService.sendMessageSync(kafkaMessage);
//            logger.info("Сообщение отправлено в Kafka: {}", savedMessage.getId());
//
//        } catch (Exception e) {
//            logger.error("Ошибка при отправке в Kafka для сообщения ID: {}", savedMessage.getId(), e);
//        }

        // Outbox pattern
        try {
            KafkaMessageDto kafkaMessage = new KafkaMessageDto(savedMessage.getId(),
                    savedMessage.getContent(),
                    savedMessage.getCreatedAt());

            // Сериализуем в JSON
            String payload = objectMapper.writeValueAsString(kafkaMessage);

            // Сохраняем в outbox
            OutboxMessage outboxMessage = new OutboxMessage(
                    message.getId(),
                    "MESSAGE_CREATED",
                    payload
            );
            outboxRepository.save(outboxMessage);
            logger.info("Outbox сообщение создано для message ID: {}", message.getId());

        } catch (JsonProcessingException e) {
            logger.error("Ошибка сериализации события для message ID: {}", message.getId(), e);
            throw new RuntimeException("Failed to create outbox message", e);
        }
        return savedMessage;
    }

    public List<MessageDto> getAll(){
        return messageRepository.findAll().stream().map(n ->new MessageDto(n.getContent())).toList();
    }

}
