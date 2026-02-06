package com.aexp.acq.go2.config;

import com.aexp.acq.go2.base.App;

import java.lang.reflect.Field;

public final class Defaults {

  private Defaults() {
  }

  /**
   * Applies defaults from App into any null/blank fields in the typed config object.
   * Defaults keys are explicit vai @DefaultKey to avoid guessing/camel-case issues.
   */
  public static <T> T apply(T cfg) {
    for (Field field : cfg.getClass().getDeclaredFields()) {
      field.setAccessible(true);

      Object current;
      try {
        current = field.get(cfg);
      }
      catch (IllegalAccessException e) {
        throw new RuntimeException("Failed reading field: " + field.getName(), e);
      }

      boolean missing = (current == null) || (current instanceof String s && s.isEmpty());
      if (!missing) {
        continue;
      }

      String key = KeyFor(field);
      String def = App.instance().getProperty(key);
      if (def == null || def.isEmpty()) {
        continue;
      }

      try {
        setField(cfg, field, def);
      }
      catch (Exception e) {
        throw new IllegalArgumentException("Invalid default for '" + key + "': " + def, e);
      }
    }
    return cfg;
  }

  private static String KeyFor(Field field) {
    DefaultKey annotation = field.getAnnotation(DefaultKey.class);
    if (annotation == null || annotation.value().isBlank()) {
      throw new IllegalArgumentException("Missing @DefaultKey on field: " + field.getName());
    }
    return annotation.value();
  }

  private static <T> void setField(T cfg, Field field, String def) throws IllegalAccessException {
    Class<?> type = field.getType();

    if (type.equals(String.class)) {
      field.set(cfg, def);
    }
    else if (type.equals(int.class) || type.equals(Integer.class)) {
      field.set(cfg, Integer.parseInt(def.trim()));
    }
    else if (type.equals(long.class) || type.equals(Long.class)) {
      field.set(cfg, Long.parseLong(def.trim()));
    }
    else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
      field.set(cfg, Boolean.parseBoolean(def.trim()));
    }
    else if (type.equals(double.class) || type.equals(Double.class)) {
      field.set(cfg, Double.parseDouble(def.trim()));
    }
    else {
      throw new IllegalArgumentException("Unsupported default type: " + type.getName() + " for field: " + field.getName());
    }
  }

}
