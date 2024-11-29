package com.aexp.acq.go2.utils;

import java.util.HashMap;
import java.util.Map;

public class CommonConstants {

  public static class Actions {

    private static Map<String, String> actions = null;

    static {
      actions = new HashMap<>();
      // Add actions here
      actions.put("AssignReviewers", "com.aexp.acq.go2.github_actions.AssignReviewers");
      actions.put("CheckStyle", "com.aexp.acq.go2.github_actions.CheckStyle");
      actions.put("MavenCLI", "com.aexp.acq.go2.github_actions.MavenCLI");
      actions.put("PurgeStaleBranches", "com.aexp.acq.go2.github_actions.PurgeStaleBranches");
      actions.put("PurgeStalePRs", "com.aexp.acq.go2.github_actions.PurgeStalePRs");
      actions.put("SyncBranch", "com.aexp.acq.go2.github_actions.SyncBranch");
    }

    public static String getAction(String action) {
      return actions.get(action);
    }

  }

}
