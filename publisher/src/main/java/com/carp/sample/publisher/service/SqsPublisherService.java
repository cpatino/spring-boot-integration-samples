package com.carp.sample.publisher.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Profile("aws")
@Service
class SqsPublisherService implements PublisherService {

    private static final Logger log = LoggerFactory.getLogger(SqsPublisherService.class);
    private final SqsClient sqsClient;

    public SqsPublisherService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
    }

    @Override
    public boolean publish(String to, String content) {
        try {
            var queueUrl = sqsClient.getQueueUrl(builder -> builder.queueName(to)).queueUrl();
            return sendMessage(queueUrl, content);
        } catch (QueueDoesNotExistException ex) {
            log.info("Queue '{}' does not exist.", to);
            return false;
        }
    }

    private boolean sendMessage(String queueUrl, String content) {
        var request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(content)
                .build();

        var response = sqsClient.sendMessage(request);
        return response.sdkHttpResponse().isSuccessful();
    }
}
