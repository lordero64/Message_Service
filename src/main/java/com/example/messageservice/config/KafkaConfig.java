package com.example.messageservice.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${app.kafka.topic.messages:messages-topic}")
    private String messagesTopic;

    @Value("${app.kafka.topic.dlx:messages-dlx}")
    private String dlxTopic;

    @Bean
    public NewTopic messageTopic() {
        return TopicBuilder.name(messagesTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic messageDlxTopic() {
        return TopicBuilder.name(dlxTopic)
                .partitions(1)
                .replicas(1)
                .build();
    }
}