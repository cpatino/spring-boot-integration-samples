package com.carp.sample.publisher.service;

import com.carp.sample.publisher.exception.MessageNotPublishedException;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("aws")
@Service
class SnsPublisherService implements PublisherService {

    private static final String TOPIC_NAME = "my-topic";
    private static final String MECHANISM = "BROADCAST";
    private static final Logger log = LoggerFactory.getLogger(SnsPublisherService.class);

    private final SnsTemplate snsTemplate;

    SnsPublisherService(SnsTemplate snsTemplate) {
        this.snsTemplate = snsTemplate;
    }

    @Override
    public boolean mustPublish(String mechanism) {
        return MECHANISM.equalsIgnoreCase(mechanism);
    }

    @Override
    public void publish(String content) {
        try {
            snsTemplate.sendNotification(TOPIC_NAME, content, null);
        } catch (Exception e) {
            log.error("Failed to publish message to SNS topic: {}", e.getMessage(), e);
            throw new MessageNotPublishedException();
        }
    }
}
