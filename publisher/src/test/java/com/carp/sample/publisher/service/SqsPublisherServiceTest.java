package com.carp.sample.publisher.service;

import com.carp.sample.publisher.exception.MessageNotPublishedException;
import io.awspring.cloud.sqs.operations.SqsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SqsPublisherServiceTest {

    private SqsPublisherService service;
    private SqsTemplate sqsTemplate;

    @BeforeEach
    void setUp() {
        sqsTemplate = Mockito.mock(SqsTemplate.class);
        service = new SqsPublisherService(sqsTemplate);
    }

    @Test
    void givenSuccessfulResponse_whenPublish_thenDoNotThrowException() {
        assertDoesNotThrow(() -> service.publish("test"));
    }

    @Test
    void givenException_whenPublish_thenThrowMessageNotPublishedException() {
        when(sqsTemplate.send(any())).thenThrow(RuntimeException.class);
        assertThrows(MessageNotPublishedException.class, () -> service.publish("runtime-exception"));
    }
}