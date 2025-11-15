package com.example.messageservice;

import com.example.messageservice.controller.MessageController;
import com.example.messageservice.dto.MessageDto;
import com.example.messageservice.entity.Message;
import com.example.messageservice.service.MessageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private MessageService messageService;

    @InjectMocks
    private MessageController messageController;

    @Test
    void createMessage_WithValidDto_ShouldReturnSavedMessage() {

        MessageDto requestDto = new MessageDto("Тестовое сообщение");
        Message expectedMessage = new Message("Тестовое сообщение");
        expectedMessage.setId(1L);
        expectedMessage.setCreatedAt(LocalDateTime.now());

        when(messageService.createMessage(requestDto)).thenReturn(expectedMessage);


        MessageDto response = messageController.createMessage(requestDto);


        assertNotNull(response);

        verify(messageService, times(1)).createMessage(requestDto);
    }
}