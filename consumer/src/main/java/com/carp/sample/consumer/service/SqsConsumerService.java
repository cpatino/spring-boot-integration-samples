package com.carp.sample.consumer.service;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("aws")
@Service
class SqsConsumerService implements ConsumerService {

    @Override
    @SqsListener("my-queue")
    public void consume(String content) {
        System.out.println("Received message: " + content);
    }
}
