package com.carp.sample.publisher.controller;

import com.carp.sample.publisher.dto.EventRequestDto;
import com.carp.sample.publisher.service.PublisherService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/publisher")
public class PublisherController {

    private final PublisherService service;

    public PublisherController(PublisherService service) {
        this.service = service;
    }

    @PostMapping("/publish")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendMessage(@RequestBody EventRequestDto request) {
        service.publish(request.content());
    }
}
