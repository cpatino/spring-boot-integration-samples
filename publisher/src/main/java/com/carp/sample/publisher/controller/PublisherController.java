package com.carp.sample.publisher.controller;

import com.carp.sample.publisher.dto.EventDto;
import com.carp.sample.publisher.service.PublisherStrategy;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publisher")
public class PublisherController {

    private final PublisherStrategy publisher;

    public PublisherController(PublisherStrategy publisher) {
        this.publisher = publisher;
    }

    @PostMapping("/publish")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendMessage(@RequestBody EventDto request) {
        publisher.publish(request.mechanism(), request.content());
    }
}
