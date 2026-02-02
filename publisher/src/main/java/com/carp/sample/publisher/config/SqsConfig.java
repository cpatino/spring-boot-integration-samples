package com.carp.sample.publisher.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

@Profile("aws")
@Configuration
class SqsConfig {

    @Bean
    public SqsClient sqsClient(@Value("${aws.endpoint}") String endpoint,
                               @Value("${aws.region}") String region,
                               @Value("${aws.accessKeyId}") String accessKeyId,
                               @Value("${aws.secretAccessKey}") String secretAccessKey) {
        return SqsClient.builder()
                .endpointOverride(URI.create(endpoint))
                .region(Region.of(region))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretAccessKey)))
                .build();
    }
}