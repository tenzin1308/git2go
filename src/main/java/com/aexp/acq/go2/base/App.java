package com.aexp.acq.go2.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
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
    logger.info("App initialized with appName: " + appName);
  }

  public void loadProperties(String resourcePath) {
    logger.info("Loading properties from: " + resourcePath);
    try (InputStream inputStream = App.class.getClassLoader().getResourceAsStream(resourcePath)) {
      properties.load(inputStream);
    }
    catch (Exception e) {
      logger.error("Error loading properties", e);
      throw new RuntimeException("Error loading properties", e);
    }


  }

  public String getProperty(String key) {
    return properties.getProperty(key);
  }

}
