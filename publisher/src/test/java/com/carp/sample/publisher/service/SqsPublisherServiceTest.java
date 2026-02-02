package com.carp.sample.publisher.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = SqsPublisherService.class)
@ActiveProfiles("aws")
class SqsPublisherServiceTest {

    @Autowired
    private SqsPublisherService service;
    @MockitoBean
    private SqsClient sqsClient;
    private SdkHttpResponse mockSdkHttpResponse;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        var mockQueueUrlResponse = Mockito.mock(GetQueueUrlResponse.class);
        when(sqsClient.getQueueUrl(any(Consumer.class))).thenReturn(mockQueueUrlResponse);
        when(mockQueueUrlResponse.queueUrl()).thenReturn("https://sqs.us-east-1.amazonaws.com/123456789012/existing-queue");
        var mockSendMessageResponse = Mockito.mock(SendMessageResponse.class);
        when(sqsClient.sendMessage(any(SendMessageRequest.class))).thenReturn(mockSendMessageResponse);
        mockSdkHttpResponse = Mockito.mock(SdkHttpResponse.class);
        when(mockSendMessageResponse.sdkHttpResponse()).thenReturn(mockSdkHttpResponse);
    }

    @Test
    @SuppressWarnings("unchecked")
    void givenUnknownQueue_whenPublish_thenReturnFalse() {
        when(sqsClient.getQueueUrl(any(Consumer.class))).thenThrow(QueueDoesNotExistException.class);

        assertFalse(service.publish("unknown-queue", "test message"));
    }

    @Test
    void givenExistingQueueAndSuccessfulResponse_whenPublish_thenReturnTrue() {
        when(mockSdkHttpResponse.isSuccessful()).thenReturn(true);

        assertTrue(service.publish("existing-queue", "test message"));
    }

    @Test
    void givenExistingQueueAndUnsuccessfulResponse_whenPublish_thenReturnFalse() {
        when(mockSdkHttpResponse.isSuccessful()).thenReturn(false);

        assertFalse(service.publish("existing-queue", "test message"));
    }
}