package com.example.test.control.exception;

import com.example.test.control.exception.api.BadRequestException;

public class DivisionByZeroException extends BadRequestException {
  public DivisionByZeroException() {
    super("Divisor cannot be zero.");
  }
}
