package com.example.test.boundary.rest.handler;

import com.example.test.boundary.dto.ErrorResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.exc.MismatchedInputException;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @NonNull MethodArgumentNotValidException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        List<String> errors = new ArrayList<>();
        for (FieldError fieldError: ex.getBindingResult().getFieldErrors()) {
            errors.add("Validation failed for field: '" + fieldError.getField() + "' message: " + fieldError.getDefaultMessage());
        }
        for (ObjectError objectError: ex.getBindingResult().getGlobalErrors()) {
            errors.add(objectError.getDefaultMessage());
        }

        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                status.value(),
                ((HttpStatus) status).getReasonPhrase(),
                errors,
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );

        return ResponseEntity.status(status).body(response);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @NonNull HttpMessageNotReadableException ex,
            @NonNull HttpHeaders headers,
            @NonNull HttpStatusCode status,
            @NonNull WebRequest request
    ) {
        ErrorResponse response = new ErrorResponse(
                Instant.now(),
                status.value(),
                ((HttpStatus) status).getReasonPhrase(),
                new ArrayList<>(List.of(buildMessage(ex))),
                ((ServletWebRequest) request).getRequest().getRequestURI()
        );
        return ResponseEntity.status(status).body(response);
    }

    private String buildMessage(HttpMessageNotReadableException ex) {
        Throwable cause = ex.getMostSpecificCause();

        if (cause instanceof MismatchedInputException mismatchedInputException) {
            return mismatchedInputException.getPath()
                    .stream()
                    .map(JacksonException.Reference::getPropertyName)
                    .filter(Objects::nonNull)
                    .findFirst()
                    .map(field -> "Field '" + field + "' has invalid type.")
                    .orElse("Request body contains invalid JSON.");
        }

        return "Request body contains invalid JSON.";
    }
}
