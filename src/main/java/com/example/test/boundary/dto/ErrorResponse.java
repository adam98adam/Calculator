package com.example.test.boundary.dto;

import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String errorName,
        List<String> messages,
        String path
) {
    public ErrorResponse {
        messages = List.copyOf(messages);
    }

    public static ErrorResponse of(HttpStatus status, List<String> messages, String path) {
        return new ErrorResponse(
                Instant.now(),
                status.value(),
                status.getReasonPhrase(),
                messages,
                path
        );
    }
}
