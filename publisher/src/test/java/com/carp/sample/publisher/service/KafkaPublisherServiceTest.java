package com.carp.sample.publisher.service;

import com.carp.sample.publisher.exception.MessageNotPublishedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.kafka.core.KafkaTemplate;

import static com.carp.sample.publisher.domain.PublisherMechanism.BROADCAST;
import static com.carp.sample.publisher.domain.PublisherMechanism.ONE_RECEIVER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;

class KafkaPublisherServiceTest {

    private KafkaPublisherService kafkaPublisherService;
    private KafkaTemplate<String, String> template;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        template = Mockito.mock(KafkaTemplate.class);
        kafkaPublisherService = new KafkaPublisherService(template);
    }

    @Test
    void givenBroadcastMechanism_whenMustPublish_thenReturnTrue() {
        assertTrue(kafkaPublisherService.mustPublish(BROADCAST));
    }

    @Test
    void givenDifferentMechanism_whenMustPublish_thenReturnFalse() {
        assertFalse(kafkaPublisherService.mustPublish(ONE_RECEIVER));
    }

    @Test
    void givenSuccessfulResponse_whenPublish_thenDoNotThrowException() {
        assertDoesNotThrow(() -> kafkaPublisherService.publish("test"));
    }

    @Test
    void givenException_whenPublish_thenThrowRuntimeException() {
        doThrow(RuntimeException.class).when(template).send(anyString(), anyString());
        assertThrows(MessageNotPublishedException.class, () -> kafkaPublisherService.publish("test"));
    }
}
