package com.carp.sample.publisher.service;

import com.carp.sample.publisher.domain.PublisherMechanism;

public interface PublisherService {

    boolean mustPublish(PublisherMechanism mechanism);

    void publish(String content);
}
