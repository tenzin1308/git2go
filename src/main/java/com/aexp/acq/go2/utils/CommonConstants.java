package com.aexp.acq.go2.utils;

import com.aexp.acq.go2.validator.RequestValidatorAssignReviewers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

    // get all the keys
    public static Set<String> getAllAction() {
      return actions.keySet();
    }

  }

  public static class BizAppValidator {

    private static Map<String, Object> bizAppValidator = null;

    static {
      bizAppValidator = new HashMap<>();
      // Add actions here
      bizAppValidator.put("AssignReviewers", new RequestValidatorAssignReviewers());
    }

    public static Object getValidator(String action) {
      return bizAppValidator.get(action);
    }

  }

}
