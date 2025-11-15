package com.example.messageservice.dto;

import java.time.LocalDateTime;

public class KafkaMessageDto {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String eventType = "MESSAGE_CREATED";

    public KafkaMessageDto() {
    }

    public KafkaMessageDto(Long id, String content, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    @Override
    public String toString() {
        return "KafkaMessageDto{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", createdAt=" + createdAt +
                ", eventType='" + eventType + '\'' +
                '}';
    }
}
