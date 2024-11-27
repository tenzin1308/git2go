package com.aexp.acq.go2.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to track the development status of classes or methods.
 */
@Target({ElementType.TYPE, ElementType.METHOD}) // Applicable to classes and methods
@Retention(RetentionPolicy.RUNTIME) // Retained at runtime for reflection
public @interface Status {

  /**
   * The current status of the class or method.
   * Possible values are: "UNDER_DEVELOPMENT", "TESTING_IN_PROGRESS", "PRODUCTION_READY"
   */
  String value();

  /**
   * Additional description providing context about the status.
   */
  String description() default "";

  /**
   * The version in which this feature will or was marked as completed.
   */
  String version() default "";

  /**
   * Indicates if the feature is experimental and may change in the future.
   * Default value is false.
   */
  boolean experimental() default false;

  /**
   * A priority level for tracking work.
   * Possible values are: "LOW", "MEDIUM", "HIGH"
   */
  String priority() default "MEDIUM";

}
