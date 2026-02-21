package com.carp.sample.publisher.dto;

public record EventDto(String mechanism, String content) {

    public EventDto {
        if (mechanism == null || mechanism.isBlank()) {
            throw new IllegalArgumentException("Mechanism must not be null or blank");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content must not be null or blank");
        }
    }
}
