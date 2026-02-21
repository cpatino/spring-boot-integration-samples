package com.carp.sample.publisher.service;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PublisherStrategy {

    private final Set<PublisherService> publishers;

    public PublisherStrategy(Set<PublisherService> publishers) {
        this.publishers = publishers;
    }

    public void publish(String mechanism, String content) {
        publishers.stream()
                .filter(p -> p.mustPublish(mechanism))
                .forEach(p -> p.publish(content));
    }
}
