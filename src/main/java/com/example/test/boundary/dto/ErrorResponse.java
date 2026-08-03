package com.example.test.boundary.dto;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String errorName,
        List<String> messages,
        String path
) {}
