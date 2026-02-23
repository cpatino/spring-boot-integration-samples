package com.carp.sample.publisher.domain;

import com.carp.sample.publisher.service.PublisherService;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PublisherStrategy {

    private final Set<PublisherService> publishers;

    public PublisherStrategy(Set<PublisherService> publishers) {
        this.publishers = publishers;
    }

    public void publish(String mechanism, String content) {
        var publisherMechanism = PublisherMechanism.valueOf(mechanism);
        publishers.stream()
                .filter(p -> p.mustPublish(publisherMechanism))
                .forEach(p -> p.publish(content));
    }
}
