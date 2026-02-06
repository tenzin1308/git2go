package com.aexp.acq.go2.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

public final class ConfigLoader {

  private static final ObjectMapper STRICT = new ObjectMapper(new YAMLFactory())
          .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);


  private static final ObjectMapper LENIENT = new ObjectMapper(new YAMLFactory())
          .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
          .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  private ConfigLoader() {
  }

  public static <T> T parseStrict(String yaml, Class<T> clazz) {
    try {
      return STRICT.readValue(yaml, clazz);
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Invalid YAML config: " + e.getMessage(), e);
    }
  }

  @SuppressWarnings("unchecked")
  public static Map<String, Object> extractPlatformOverrides(String yaml) {
    try {
      Map<String, Object> root = LENIENT.readValue(yaml, LinkedHashMap.class);
      if (root == null) {
        return Collections.emptyMap();
      }
      Object platform = root.get("platform");
      if (platform instanceof Map<?, ?> map) {
        Map<String, Object> out = new LinkedHashMap<>();
        map.forEach((key, value) -> out.put(String.valueOf(key), value));
        return out;
      }
      return Collections.emptyMap();
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Invalid YAML (failed reading platform): " + e.getMessage(), e);
    }
  }

  @SuppressWarnings("unchecked")
  public static String removePlatformBlock(String yaml) {
    try {
      Map<String, Object> root = LENIENT.readValue(yaml, LinkedHashMap.class);
      if (root == null) {
        return yaml;
      }
      root.remove("platform");
      return LENIENT.writeValueAsString(root);
    }
    catch (Exception e) {
      throw new IllegalArgumentException("Invalid YAML (failed removing platform): " + e.getMessage(), e);
    }
  }

}
