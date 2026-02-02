package com.carp.sample.publisher.controller;

import com.carp.sample.publisher.service.PublisherService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/publisher")
public class PublisherController {

    private final PublisherService service;

    public PublisherController(PublisherService service) {
        this.service = service;
    }

    @PostMapping("/publish")
    public boolean sendMessage(@RequestParam String to, @RequestParam String content) {
        return service.publish(to, content);
    }
}
