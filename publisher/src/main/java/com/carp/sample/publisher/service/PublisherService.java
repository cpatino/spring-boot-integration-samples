package com.carp.sample.publisher.service;

public interface PublisherService {

    boolean mustPublish(String mechanism);

    void publish(String content);
}
