package com.example.messageservice.config;

import com.example.messageservice.dto.KafkaMessageDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

//@Service
//public class KafkaMessageService {
//
//    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageService.class);
//
//    private final KafkaTemplate<String, KafkaMessageDto> kafkaTemplate;
//
//    @Value("${app.kafka.topic}")
//    private String topicName;
//
//    public KafkaMessageService(KafkaTemplate<String, KafkaMessageDto> kafkaTemplate) {
//        this.kafkaTemplate = kafkaTemplate;
//    }
//
//    public void sendMessageAsync(KafkaMessageDto message) {
//        try {
//            CompletableFuture<SendResult<String, KafkaMessageDto>> future =
//                    kafkaTemplate.send(topicName, message);
//
//            future.whenComplete((result, ex) -> {
//                if (ex == null) {
//                    logger.info("Сообщение отправлено в Kafka: [{}] offset: [{}]",
//                            message.getContent(), result.getRecordMetadata().offset());
//                } else {
//                    logger.error("Ошибка отправки сообщения в Kafka: {}", message.getContent(), ex);
//                }
//            });
//
//        } catch (Exception e) {
//            logger.error("Исключение при отправке в Kafka: {}", e.getMessage(), e);
//        }
//    }
//
//
//    public boolean sendMessageSync(KafkaMessageDto messageDto) {
//        try {
//            SendResult<String, KafkaMessageDto> sendResult = kafkaTemplate.send(topicName, messageDto).get();
//
//            logger.info("Сообщение синхронно отпраленно в Kafka: [{}] partition: [{}] offset: [{}]",
//                    messageDto.getContent(),
//                    sendResult.getRecordMetadata().partition(),
//                    sendResult.getRecordMetadata().offset());
//            return true;
//
//
//        } catch (Exception e) {
//            logger.error("Ошибка синхронной отправки в Kafka: {}", e.getMessage(), e);
//            return false;
//        }
//    }
//}
