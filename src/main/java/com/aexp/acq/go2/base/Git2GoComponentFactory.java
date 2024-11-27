package com.aexp.acq.go2.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Git2GoComponentFactory {

  private static Logger logger = LoggerFactory.getLogger(Git2GoComponentFactory.class);
  private static Git2GoComponentFactory factory = new Git2GoComponentFactory();

  private Git2GoComponentFactory() {
    // nothing to do
  }

  public static Git2GoComponentFactory instance() {
    return factory;
  }

  private static boolean isMock() {
    return Boolean.getBoolean(App.instance().getProperty("is_mock_enabled"));
  }

  public Object getComponent(String compName) {
    Object obj = null;

    try {
      Class<?> compClass = Class.forName(compName);
      if (isMock() && Interaction.class.isAssignableFrom(compClass)) {
        compName += "Mock";
      }
      Class<?> clazz = Class.forName(compName);
      Constructor<?> ctor = clazz.getConstructor(String.class);
      obj = ctor.newInstance(compName);
    }
    catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
           InvocationTargetException e) {
      logger.error("Error creating component -> {}", e.getMessage());
      System.exit(1);
    }

    return obj;
  }

}
