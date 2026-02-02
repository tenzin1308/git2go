package com.aexp.acq.go2.config;

public class ConfigValidator {

  public static void validate(ActionConfig config) {
    if (config == null) {
      throw new IllegalArgumentException("Config is missing");
    }
    if (config.getAction() == null) {
      throw new IllegalArgumentException("Action name is required");
    }
  }

}
