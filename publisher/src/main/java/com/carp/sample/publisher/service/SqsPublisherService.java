package com.carp.sample.publisher.service;

import com.carp.sample.publisher.exception.MessageNotPublishedException;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("aws")
@Service
class SqsPublisherService implements PublisherService {

    private static final String QUEUE_NAME = "my-queue";
    private static final String MECHANISM = "ONE_RECEIVER";
    private static final Logger log = LoggerFactory.getLogger(SqsPublisherService.class);

    private final SqsTemplate sqsTemplate;

    public SqsPublisherService(SqsTemplate sqsTemplate) {
        this.sqsTemplate = sqsTemplate;
    }

    @Override
    public boolean mustPublish(String mechanism) {
        return MECHANISM.equalsIgnoreCase(mechanism);
    }

    @Override
    public void publish(String content) {
        try {
            sqsTemplate.send(to -> to.queue(QUEUE_NAME)
                    .payload(content)
            );
        } catch (Exception e) {
            log.error("Failed to publish message to SQS queue: {}", e.getMessage(), e);
            throw new MessageNotPublishedException();
        }
    }
}
