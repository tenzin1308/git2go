package com.aexp.acq.go2.core;

import java.util.ServiceLoader;

public class ActionRegistry {

  public static Action get(String name) {
    for (Action action : ServiceLoader.load(Action.class)) {
      if (action.name().equalsIgnoreCase(name)) {
        return action;
      }
    }
    throw new IllegalArgumentException("Unknown action: " + name);
  }

}
