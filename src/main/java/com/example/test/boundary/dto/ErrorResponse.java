package com.example.test.boundary.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.List;
import org.springframework.http.HttpStatus;

@Schema(description = "Error response returned when a request cannot be processed")
public record ErrorResponse(
    @Schema(
            description = "Time when the error response was created",
            example = "2026-08-05T10:40:00Z")
        Instant timestamp,
    @Schema(description = "HTTP status code", example = "400") int status,
    @Schema(description = "HTTP status reason phrase", example = "Bad Request") String error,
    @Schema(description = "Detailed error messages") List<String> messages,
    @Schema(description = "Request path that caused the error", example = "/div") String path) {
  public ErrorResponse {
    messages = List.copyOf(messages);
  }

  public static ErrorResponse of(HttpStatus status, List<String> messages, String path) {
    return new ErrorResponse(
        Instant.now(), status.value(), status.getReasonPhrase(), messages, path);
  }
}
