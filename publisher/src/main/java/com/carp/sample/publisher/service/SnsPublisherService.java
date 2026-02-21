package com.carp.sample.publisher.service;

import com.carp.sample.publisher.exception.MessageNotPublishedException;
import io.awspring.cloud.sns.core.SnsNotification;
import io.awspring.cloud.sns.core.SnsOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("aws")
@Service
class SnsPublisherService implements PublisherService {

    private static final String TOPIC_ARN = "arn:aws:sns:eu-west-1:000000000000:my-topic";
    private static final String MECHANISM = "BROADCAST";
    private static final Logger log = LoggerFactory.getLogger(SnsPublisherService.class);

    private final SnsOperations snsOperations;

    SnsPublisherService(SnsOperations snsOperations) {
        this.snsOperations = snsOperations;
    }

    @Override
    public boolean mustPublish(String mechanism) {
        return MECHANISM.equalsIgnoreCase(mechanism);
    }

    @Override
    public void publish(String content) {
        try {
            SnsNotification<String> notification = SnsNotification.builder(content).build();
            snsOperations.sendNotification(TOPIC_ARN, notification);
        } catch (Exception e) {
            log.error("Failed to publish message to SNS topic: {}", e.getMessage(), e);
            throw new MessageNotPublishedException();
        }
    }
}
