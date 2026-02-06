package com.aexp.acq.go2.config;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;
import java.util.stream.Collectors;

public final class ValidationUtil {

  private static final ValidatorFactory FACTORY = Validation.buildDefaultValidatorFactory();
  private static final Validator VALIDATOR = FACTORY.getValidator();

  private ValidationUtil() {
  }

  public static <T> void validateOrThrow(T cfg) {
    Set<ConstraintViolation<T>> constraintViolations = VALIDATOR.validate(cfg);
    if (!constraintViolations.isEmpty()) {
      String msg = constraintViolations.stream()
              .map(v -> v.getPropertyPath() + " " + v.getMessage())
              .collect(Collectors.joining("; "));
      throw new IllegalArgumentException("Invalid config: " + msg);
    }
  }

  public static void shutdown() {
    FACTORY.close();
  }

}
