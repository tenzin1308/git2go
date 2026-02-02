package com.aexp.acq.go2.core;

public interface Action {

  String name();

  void execute(ActionContext context);

}
