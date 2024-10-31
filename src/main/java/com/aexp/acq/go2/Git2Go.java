package com.aexp.acq.go2;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.github_actions.StaleAction;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Git2Go {

  public static void main(String[] args) {
    init();

    System.out.println("Hello and welcome!");

    new StaleAction("com.aexp.acq.go2.github_actions.StaleAction").execute();
  }

  private static void init() {
    App.init(Git2Go.class.getSimpleName());
    App.instance().loadProperties("config.properties");
  }

}
