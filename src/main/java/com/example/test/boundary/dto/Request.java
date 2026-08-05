package com.example.test.boundary.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Calculator operation request")
public record Request(double val1, double val2) {}
