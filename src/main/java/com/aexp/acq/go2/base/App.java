package com.aexp.acq.go2.base;

import com.aexp.acq.go2.utils.BaseUtils;
import com.americanexpress.unify.jdocs.JDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

public final class App {

  private static final Logger logger = LoggerFactory.getLogger(App.class);
  private static final App instance = new App();
  private final Properties properties = new Properties();
  private String appName = null;

  public static App instance() {
    return instance;
  }

  public static void init(String appName) {
    App.instance().appName = appName;
    logger.info("App initialized with appName: {}", appName);
  }

  public static void loadDocumentModels(String propName, String path) {
    List<String> models = new ArrayList<>();
    String s = App.instance().getProperty(propName);
    String[] tokens = s.split(",");
    for (String token : tokens) {
      token = token.trim();
      if (token.isEmpty() == false) {
        models.add(token);
      }
    }

    for (String type : models) {
      String json = BaseUtils.getResourceAsString(App.class, path + type + ".json");
      JDocument.loadDocumentModel(type, json);
    }
  }

  /**
   * Import all env vars into dot-key properties (Lowest Precedent).
   * Naming:
   * ENV_NAME -> env.name
   * Example:
   * GITHUB_SERVER_URL -> github.server.url
   */
  public void loadEnvironmentProperties() {
    logger.info("Loading Env Properties");
    for (Map.Entry<String, String> entry : System.getenv().entrySet()) {
      String key = entry.getKey();
      String value = entry.getValue();
      if (value == null) {
        continue;
      }
      properties.setProperty(normalizeKey(key), value);
    }
  }

  /**
   * Import all config properties overwrites env defaults (Second Highest Precedent).
   */
  public void loadProperties(String resourcePath) {
    logger.info("Loading properties -> {}", resourcePath);
    try (InputStream inputStream = App.class.getClassLoader().getResourceAsStream(resourcePath)) {
      if (inputStream == null) {
        logger.info("No {} on classpath; skipping", resourcePath);
        return;
      }
      Properties fileProperties = new Properties();
      fileProperties.load(inputStream);
      fileProperties.forEach((key, value) -> properties.setProperty(String.valueOf(key), String.valueOf(value)));
    }
    catch (Exception e) {
      logger.error("Error loading properties", e);
      throw new RuntimeException("Failed loading properties : " + resourcePath, e);
    }
  }

  /**
   * Platform overrides env + properties (Highest Precedent).
   * YAML keys normalize same way: snake_case -> dot.keys
   */
  public void applyPlatformOverrides(Map<String, Object> platform) {
    if (platform == null || platform.isEmpty()) {
      return;
    }

    for (Map.Entry<String, Object> entry : platform.entrySet()) {
      String rawKey = entry.getKey();
      Object rawValue = entry.getValue();

      if (rawKey == null || rawValue == null) {
        continue;
      }

      String value = String.valueOf(rawValue).trim();
      if (value.isBlank()) {
        continue;
      }

      properties.setProperty(normalizeKey(rawKey), value);

    }
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

  public String getProperty(String key, String defaultValue) {
    return properties.getProperty(key, defaultValue);
  }

  public int getInt(String key, int defaultValue) {
    String value = getProperty(key);
    if (value == null || value.isBlank()) {
      return defaultValue;
    }
    return Integer.parseInt(value.trim());
  }

  public boolean getBoolean(String key, boolean defaultValue) {
    String value = getProperty(key);
    if (value == null || value.isBlank()) {
      return defaultValue;
    }
    return Boolean.parseBoolean(value.trim());
  }

  private String normalizeKey(String key) {
    return key.toLowerCase(Locale.ROOT).replace('_', '.');
  }

}
