package com.example.messageservice.repository;

import com.example.messageservice.entity.OutboxMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutboxRepository extends JpaRepository<OutboxMessage, Long> {

    List<OutboxMessage> findByStatusOrderByCreatedAtAsc(OutboxMessage.OutboxStatus status);

    @Query("SELECT om FROM OutboxMessage om WHERE om.status = 'PENDING' AND om.createdAt < :threshold ORDER BY om.createdAt ASC")
    List<OutboxMessage> findPendingMessagesOlderThan(@Param("threshold") LocalDateTime threshold);

    @Modifying
    @Query("UPDATE OutboxMessage om SET om.status = 'FAILED' WHERE om.status = 'PENDING' AND om.createdAt < :threshold")
    void markOldPendingAsFailed(@Param("threshold") LocalDateTime threshold);

    @Modifying
    @Query("DELETE FROM OutboxMessage om WHERE om.status = 'PROCESSED' AND om.processedAt < :threshold")
    void deleteOldProcessedMessages(@Param("threshold") LocalDateTime threshold);
}