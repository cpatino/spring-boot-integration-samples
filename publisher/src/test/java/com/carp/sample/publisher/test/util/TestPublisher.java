package com.carp.sample.publisher.test.util;

import com.carp.sample.publisher.service.PublisherService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
class TestPublisher implements PublisherService {

    @Override
    public boolean publish(String to, String content) {
        return true;
    }
}
