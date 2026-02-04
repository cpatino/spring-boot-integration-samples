package com.carp.sample.publisher.service;

import com.carp.sample.publisher.exception.MessageNotPublishedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import software.amazon.awssdk.http.SdkHttpResponse;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.QueueDoesNotExistException;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageResponse;

import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class SqsPublisherServiceTest {

    private SqsPublisherService service;
    private SqsClient sqsClient;
    private SdkHttpResponse mockSdkHttpResponse;

    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() {
        sqsClient = Mockito.mock(SqsClient.class);
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

        assertThrows(QueueDoesNotExistException.class, () -> service = new SqsPublisherService(sqsClient));
    }

    @Test
    void givenExistingQueueAndSuccessfulResponse_whenPublish_thenReturnTrue() {
        when(mockSdkHttpResponse.isSuccessful()).thenReturn(true);
        service = new SqsPublisherService(sqsClient);

        assertDoesNotThrow(() -> service.publish("test message"));
    }

    @Test
    void givenExistingQueueAndUnsuccessfulResponse_whenPublish_thenReturnFalse() {
        when(mockSdkHttpResponse.isSuccessful()).thenReturn(false);
        service = new SqsPublisherService(sqsClient);

        assertThrows(MessageNotPublishedException.class, () -> service.publish("test message"));
    }
}