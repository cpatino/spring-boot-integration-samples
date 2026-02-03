package com.carp.sample.publisher.test.util;

import com.carp.sample.publisher.service.PublisherService;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("test")
@Service
public class TestPublisher implements PublisherService {

    public static final String SUCCESSFUL_CONTENT = "successful";

    @Override
    public boolean publish(String to, String content) {
        return SUCCESSFUL_CONTENT.equals(content);
    }
}
