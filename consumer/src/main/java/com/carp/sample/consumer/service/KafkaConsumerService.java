package com.carp.sample.consumer.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Profile("kafka")
@Service
public class KafkaConsumerService implements ConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Override
    @KafkaListener(topics = "my-topic")
    public void consume(String content) {
        log.info("Received message: {}", content);
    }
}
