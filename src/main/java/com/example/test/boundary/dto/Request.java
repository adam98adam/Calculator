package com.example.test.boundary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Calculator operation request")
public record Request(
    @Schema(description = "First value used in the calculation", example = "12.0") double val1,
    @Schema(description = "Second value used in the calculation", example = "6.0") double val2) {}
