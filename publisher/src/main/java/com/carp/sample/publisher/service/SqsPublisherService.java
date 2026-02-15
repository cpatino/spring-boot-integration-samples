package com.carp.sample.publisher.service;

import com.carp.sample.publisher.exception.MessageNotPublishedException;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("aws")
@Service
class SqsPublisherService implements PublisherService {

    private static final String QUEUE_NAME = "my-queue";

    private final SqsTemplate sqsTemplate;

    public SqsPublisherService(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    @Override
    public void publish(String content) {
        try {
            sqsTemplate.send(to -> to.queue(QUEUE_NAME)
                    .payload(content)
            );
        } catch (Exception e) {
            throw new MessageNotPublishedException();
        }
    }
}
