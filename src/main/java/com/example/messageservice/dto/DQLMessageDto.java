package com.example.messageservice.dto;

import java.time.LocalDateTime;

public class DQLMessageDto {
    private Long outboxId;
    private Long aggregateId;
    private String eventType;
    private String originalPayload;
    private LocalDateTime createdAt;
    private String failureReason;

    public DQLMessageDto() {}

    public DQLMessageDto(Long outboxId, Long aggregateId, String eventType, String originalPayload,
                      LocalDateTime createdAt, String failureReason) {
        this.outboxId = outboxId;
        this.aggregateId = aggregateId;
        this.eventType = eventType;
        this.originalPayload = originalPayload;
        this.createdAt = createdAt;
        this.failureReason = failureReason;
    }

    public Long getOutboxId() {
        return outboxId;
    }

    public void setOutboxId(Long outboxId) {
        this.outboxId = outboxId;
    }

    public Long getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(Long aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getOriginalPayload() {
        return originalPayload;
    }

    public void setOriginalPayload(String originalPayload) {
        this.originalPayload = originalPayload;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public void setFailureReason(String failureReason) {
        this.failureReason = failureReason;
    }

}
