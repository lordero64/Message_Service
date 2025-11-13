package com.example.messageservice.service;

import com.example.messageservice.dto.MessageDto;
import com.example.messageservice.entity.Message;
import com.example.messageservice.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(MessageDto messageDto){
        Message message = new Message(messageDto.getContent());
        return messageRepository.save(message);
    }

    public List<MessageDto> getAll(){
        return messageRepository.findAll().stream().map(n ->new MessageDto(n.getContent())).toList();
    }

}
