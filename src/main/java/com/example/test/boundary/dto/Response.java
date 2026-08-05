package com.example.test.boundary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Calculator operation response")
public record Response(@Schema(description = "Calculated result", example = "18.0") double value) {}
