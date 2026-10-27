package com.thermcampos.validation;

public class ValidationUtil {

  public static final String ERROR_KEY = "error";
  public static final Integer NAME_MAX_SIZE = 300;
  
  public static void notNullNorBlank(String field, String value) {
    if (value == null || value.isBlank()) {
      throw new ValidationException(field, "must not be null or empty");
    }
  }

  public static void notNull(String field, Number number) {
    if (number == null) {
      throw new ValidationException(field, "must not be null");
    }
  }

  public static void maxSize(String field, String value, int maxSize) {
    if (value.length() > maxSize) {
      throw new ValidationException(field, "size must be between 0 and " + maxSize);
    }
  }

  public static void notNegative(String field, Number number) {
    if (number.intValue() < 0) {
      throw new ValidationException(field, "must not be negative");
    }
  }
}

