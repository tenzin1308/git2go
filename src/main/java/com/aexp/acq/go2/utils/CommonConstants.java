package com.aexp.acq.go2.utils;

import java.util.HashMap;
import java.util.Map;

public class CommonConstants {

  public static class Actions {

    private static Map<String, String> actions = null;

    static {
      actions = new HashMap<>();
      // Add actions here
      actions.put("PurgeStaleBranches", "com.aexp.acq.go2.github_actions.PurgeStaleBranches");
    }

    public static String getAction(String action) {
      return actions.get(action);
    }

  }

}
