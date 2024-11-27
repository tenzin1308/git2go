package com.aexp.acq.go2.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class Git2GoComponent {

  private static Logger logger = LoggerFactory.getLogger(Git2GoComponent.class);
  private boolean isMock = false;

  protected Git2GoComponent() {
    // nothing to do
  }

  public static Git2GoComponent instance() {
    return new Git2GoComponent();
  }

  public void init() {
    isMock = isMock();
  }

  private Boolean isMock() {
    return Boolean.parseBoolean(App.instance().getProperty("is_mock"));
  }

  public Object getComponent(String compName) throws ClassNotFoundException {
    Object obj = null;
    if (isMock == true && (Interaction.class.isAssignableFrom(Class.forName(compName)) || GraphInteraction.class.isAssignableFrom(Class.forName(compName)))) {
      compName = compName + "Mock";
    }
    try {
      Class<?> clazz = Class.forName(compName);
      Constructor<?> ctor = clazz.getConstructor(String.class);
      obj = ctor.newInstance(compName);
    }
    catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException |
           InvocationTargetException e) {
      logger.error("Error creating component: {}", compName, e);
    }
    return obj;
  }

}
