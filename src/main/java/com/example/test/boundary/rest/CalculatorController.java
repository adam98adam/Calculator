package com.example.test.boundary.rest;

import com.example.test.boundary.dto.ErrorResponse;
import com.example.test.boundary.dto.Request;
import com.example.test.boundary.dto.Response;
import com.example.test.control.CalculatorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Calculator", description = "Operations for basic arithmetic calculations")
public class CalculatorController {
  private final CalculatorService calculatorService;

  public CalculatorController(CalculatorService calculatorService) {
    this.calculatorService = calculatorService;
  }

  @PostMapping(value = "/add")
  @Operation(summary = "Add two numbers", description = "Returns the sum of two values.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Sum calculated successfully",
        content = @Content(schema = @Schema(implementation = Response.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Invalid request",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  private Response add(@RequestBody Request request) {
    double value = calculatorService.add(request.val1(), request.val2());
    return new Response(value);
  }

  @GetMapping(value = "/div")
  @Operation(summary = "Divide two numbers", description = "Returns the quotient of two values.")
  @ApiResponses({
    @ApiResponse(
        responseCode = "200",
        description = "Quotient calculated successfully",
        content = @Content(schema = @Schema(implementation = Response.class))),
    @ApiResponse(
        responseCode = "400",
        description = "Invalid request",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
  })
  private Response div(@ParameterObject @ModelAttribute Request request) {
    double value = calculatorService.divide(request.val1(), request.val2());
    return new Response(value);
  }
}
