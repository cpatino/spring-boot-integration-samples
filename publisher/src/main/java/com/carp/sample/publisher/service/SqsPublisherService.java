package com.carp.sample.publisher.service;

import com.carp.sample.publisher.exception.MessageNotPublishedException;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Profile("aws")
@Service
class SqsPublisherService implements PublisherService {

    private static final String QUEUE_NAME = "my-queue";

    private final SqsClient sqsClient;
    private final String queueUrl;

    public SqsPublisherService(SqsClient sqsClient) {
        this.sqsClient = sqsClient;
        queueUrl = sqsClient.getQueueUrl(builder -> builder.queueName(QUEUE_NAME)).queueUrl();
    }

    @Override
    public void publish(String content) {
        var request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody(content)
                .build();

        var response = sqsClient.sendMessage(request);

        if (!response.sdkHttpResponse().isSuccessful()) {
            throw new MessageNotPublishedException();
        }
    }
}
