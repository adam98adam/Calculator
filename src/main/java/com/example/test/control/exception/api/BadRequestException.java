package com.example.test.control.exception.api;

/** 400 BAD REQUEST */
public class BadRequestException extends RuntimeException {

  public BadRequestException(String message) {
    super(message);
  }
}
