package com.example.messageservice.controller;

import com.example.messageservice.dto.MessageDto;
import com.example.messageservice.entity.Message;
import com.example.messageservice.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private static final Logger log = LoggerFactory.getLogger(MessageController.class);

    @Autowired
    private MessageService messageService;

    @GetMapping
    public List<MessageDto> getAll(){
       return messageService.getAll();
    }

    @PostMapping()
    public MessageDto createMessage(@RequestBody MessageDto messageDto) {
        log.info("POST payment: {}", messageDto.getContent());
        return new MessageDto((messageService.createMessage(messageDto)).getContent());
    }
}
