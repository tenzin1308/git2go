package com.aexp.acq.go2.validator;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.utils.BaseUtils;

public class RequestValidatorAssignReviewers extends RequestValidator {

  public void validateInput(Object... vargs) {
    String configPath = App.instance().getProperty("reviewers.config.path");
    if (BaseUtils.isNullOrEmpty(configPath) == true) {
      System.err.println("Reviewers config path cannot be null or empty");
      System.exit(1);
    }

    if (configPath.endsWith(".yaml") == false && configPath.endsWith(".yml") == false && configPath.endsWith(".json") == false) {
      System.err.println("Reviewers config path must be a yaml or json file");
      System.exit(1);
    }

    if (BaseUtils.isNullOrEmpty(App.instance().getProperty("reviewers.pull.request.number")) == true) {
      System.err.println("Pull request number cannot be null or empty");
      System.exit(1);
    }

    if (BaseUtils.isNullOrEmpty(App.instance().getProperty("github.base.url")) == true) {
      System.err.println("Github base url cannot be null or empty");
      System.exit(1);
    }
  }

}
