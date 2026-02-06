package com.aexp.acq.go2.core;

public interface Action<T> {

  /**
   * Unique action name used by workflows (e.g. "maven-cli").
   */
  String name();

  /**
   * Typed config class used for strict YAML parsing.
   */
  Class<T> configClass();

  /**
   * Optional additional checks beyond bean validation.
   * Default is no-op
   */
  default void validate(T config) {
    // no-op
  }

  /**
   * Execute the action using the typed, validated config.
   * <p>
   * Implementations should throw on error.
   * </p>
   */
  void execute(T config) throws Exception;

}
