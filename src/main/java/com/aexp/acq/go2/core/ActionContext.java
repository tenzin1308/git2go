package com.aexp.acq.go2.core;

import com.aexp.acq.go2.config.ActionConfig;

public class ActionContext {

  private final ActionConfig config;

  public ActionContext(ActionConfig config) {
    this.config = config;
  }

  public ActionConfig getConfig() {
    return config;
  }

}
