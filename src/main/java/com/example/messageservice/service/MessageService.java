package com.example.messageservice.service;

import com.example.messageservice.config.KafkaMessageService;
import com.example.messageservice.dto.KafkaMessageDto;
import com.example.messageservice.dto.MessageDto;
import com.example.messageservice.entity.Message;
import com.example.messageservice.repository.MessageRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private KafkaMessageService kafkaMessageService;
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    @Transactional
    public Message createMessage(MessageDto messageDto){
        logger.info("Создание сообщения: {}", messageDto.getContent());

        Message message = new Message(messageDto.getContent());
        Message savedMessage = messageRepository.save(message);

        logger.info("Сообщение сохранено в БД с ID: {}", savedMessage.getId());

        try {
            KafkaMessageDto kafkaMessage = new KafkaMessageDto(savedMessage.getId(),
                    savedMessage.getContent(),
                    savedMessage.getCreatedAt());

            kafkaMessageService.sendMessageSync(kafkaMessage);
            logger.info("Сообщение отправлено в Kafka: {}", savedMessage.getId());

        } catch (Exception e) {
            logger.error("Ошибка при отправке в Kafka для сообщения ID: {}", savedMessage.getId(), e);
        }

        return savedMessage;
    }

    public List<MessageDto> getAll(){
        return messageRepository.findAll().stream().map(n ->new MessageDto(n.getContent())).toList();
    }

}
