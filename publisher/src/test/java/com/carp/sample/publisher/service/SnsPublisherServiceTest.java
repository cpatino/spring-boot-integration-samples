package com.carp.sample.publisher.service;

import com.carp.sample.publisher.exception.MessageNotPublishedException;
import io.awspring.cloud.sns.core.SnsTemplate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.messaging.MessagingException;

import static com.carp.sample.publisher.domain.PublisherMechanism.BROADCAST;
import static com.carp.sample.publisher.domain.PublisherMechanism.ONE_RECEIVER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;

class SnsPublisherServiceTest {

    private SnsPublisherService service;
    private SnsTemplate snsTemplate;

    @BeforeEach
    void setUp() {
        snsTemplate = Mockito.mock(SnsTemplate.class);
        service = new SnsPublisherService(snsTemplate);
    }

    @Test
    void givenBroadcastMechanism_whenMustPublish_thenReturnTrue() {
        assertTrue(service.mustPublish(BROADCAST));
    }

    @Test
    void givenDifferentMechanism_whenMustPublish_thenReturnFalse() {
        assertFalse(service.mustPublish(ONE_RECEIVER));
    }

    @Test
    void givenSuccessfulResponse_whenPublish_thenDoNotThrowException() {
        assertDoesNotThrow(() -> service.publish("test"));
    }

    @Test
    void givenException_whenPublish_thenThrowMessageNotPublishedException() {
        doThrow(MessagingException.class).when(snsTemplate).sendNotification(anyString(), any(), nullable(String.class));
        assertThrows(MessageNotPublishedException.class, () -> service.publish("exception"));
    }
}