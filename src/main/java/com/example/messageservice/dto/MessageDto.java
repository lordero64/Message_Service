package com.example.messageservice.dto;

import java.time.LocalDateTime;

public class MessageDto {

    private String content;
    private LocalDateTime createdAt;

    public MessageDto() {}

    public MessageDto(String content) {
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
