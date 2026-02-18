package com.carp.sample.publisher.controller;

import com.carp.sample.publisher.dto.EventRequestDto;
import com.carp.sample.publisher.test.util.TestPublisher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.client.RestTestClient;

import static com.carp.sample.publisher.test.util.TestPublisher.SUCCESSFUL_CONTENT;

@WebMvcTest({PublisherController.class, TestPublisher.class})
@AutoConfigureRestTestClient
class PublisherControllerTest {

    private static final String PATH = "/publisher/publish";

    @Autowired
    private RestTestClient restTestClient;

    @Test
    void givenSuccessfulPublish_whenSendMessage_thenReturnsTrue() {
        restTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .build())
                .body(new EventRequestDto(SUCCESSFUL_CONTENT))
                .exchange()
                .expectStatus().isNoContent()
                .expectBody().isEmpty();
    }

    @Test
    void givenFailedPublish_whenSendMessage_thenReturnsFalse() {
        restTestClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path(PATH)
                        .build())
                .body(new EventRequestDto("failed"))
                .exchange()
                .expectStatus().is5xxServerError();
    }

    @Test
    void givenNoParameters_whenSendMessage_thenReturnsBadRequest() {
        restTestClient.post()
                .uri(PATH)
                .exchange()
                .expectStatus().isBadRequest();
    }
}