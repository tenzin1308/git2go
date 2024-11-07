package com.aexp.acq.go2.base;

import com.aexp.acq.go2.utils.BaseUtils;
import com.americanexpress.unify.jdocs.JDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class App {

  private static final Logger logger = LoggerFactory.getLogger(App.class);
  private static App instance = new App();
  private Properties properties = new Properties();
  private String appName = null;

  public static App instance() {
    return instance;
  }

  public static void init(String appName) {
    instance.appName = appName;
    logger.info("App initialized with appName: " + instance.appName);
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

  public void loadProperties(String resourcePath) {
    logger.info("Loading properties -> " + resourcePath);
    try (InputStream inputStream = App.class.getClassLoader().getResourceAsStream(resourcePath)) {
      properties.load(inputStream);
    }
    catch (Exception e) {
      logger.error("Error loading properties", e);
      throw new RuntimeException("Error loading properties", e);
    }

    properties.putAll(System.getenv());
  }

  public void loadEnvironmentProperties() {
    logger.info("Loading Env Properties");
    for (String key : properties.stringPropertyNames()) {
      String envKey = String.format("INPUT_%s", key.replace(".", "_").toUpperCase());
      String envValue = System.getenv(envKey);
      if (envValue != null) {
        properties.setProperty(key, envValue);
      }
    }
  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

}
