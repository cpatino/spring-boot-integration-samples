package com.carp.sample.publisher.service;

import com.carp.sample.publisher.domain.PublisherMechanism;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.carp.sample.publisher.domain.PublisherMechanism.BROADCAST;

@Profile("kafka")
@Service
public class KafkaPublisherService implements PublisherService {

    private static final String TOPIC_NAME = "my-topic";
    private static final Logger log = LoggerFactory.getLogger(KafkaPublisherService.class);

    private final KafkaTemplate<String, String> template;

    public KafkaPublisherService(KafkaTemplate<String, String> template) {
        this.template = template;
    }

    @Override
    public boolean mustPublish(PublisherMechanism mechanism) {
        return BROADCAST.equals(mechanism);
    }

    @Override
    public void publish(String content) {
        try {
            template.send(TOPIC_NAME, content);
        } catch (Exception e) {
            log.error("Failed to publish message to Kafka topic: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to publish message to Kafka topic", e);
        }
    }
}
