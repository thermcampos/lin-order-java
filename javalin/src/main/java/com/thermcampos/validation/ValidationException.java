package com.thermcampos.validation;

public class ValidationException extends RuntimeException {

  private final String errorKey;

  public ValidationException(String errorKey, String errorMessage) {
    super(errorMessage);
    this.errorKey = errorKey;
  }

  public String getErrorKey() {
    return errorKey;
  }
}

