package com.carp.sample.publisher.domain;

import com.carp.sample.publisher.service.PublisherService;
import com.carp.sample.publisher.test.util.TestPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.carp.sample.publisher.domain.PublisherMechanism.BROADCAST;
import static com.carp.sample.publisher.domain.PublisherMechanism.ONE_RECEIVER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PublisherStrategyTest {

    private PublisherStrategy strategy;

    @BeforeEach
    void setUp() {
        Set<PublisherService> services = Set.of(new TestPublisher());
        strategy = new PublisherStrategy(services);
    }

    @Test
    void givenUnknownMechanism_whenPublish_thenThrowException() {
        assertThrows(IllegalArgumentException.class, () -> strategy.publish("UNKNOWN", "content"));
    }

    @Test
    void givenKnownMechanismAndServiceFinishesSuccessfully_whenPublish_thenNoException() {
        assertDoesNotThrow(() -> strategy.publish(BROADCAST.name(), TestPublisher.SUCCESSFUL_CONTENT));
    }

    @Test
    void givenKnownMechanismAndServiceFails_whenPublish_thenThrowException() {
        assertThrows(RuntimeException.class, () -> strategy.publish(ONE_RECEIVER.name(), "unsuccessful"));
    }
}
