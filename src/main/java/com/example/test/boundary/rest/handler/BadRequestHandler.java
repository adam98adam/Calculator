package com.example.test.boundary.rest.handler;

import com.example.test.boundary.dto.ErrorResponse;
import com.example.test.control.exception.api.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class BadRequestHandler {
    private static final HttpStatus status = HttpStatus.BAD_REQUEST;

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(
            BadRequestException exception,
            WebRequest webRequest
    ) {
        ErrorResponse errorResponse = ErrorResponse.of(
                HttpStatus.BAD_REQUEST,
                new ArrayList<>(List.of(exception.getMessage())),
                ((ServletWebRequest) webRequest).getRequest().getRequestURI()
        );

        return ResponseEntity.badRequest().body(errorResponse);
    }

}
