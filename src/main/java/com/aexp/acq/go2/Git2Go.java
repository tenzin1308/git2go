package com.aexp.acq.go2;

import com.aexp.acq.go2.base.App;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Git2Go {

  public static void main(String[] args) {
    init();
    // Press Opt+Enter with your caret at the highlighted text to see how
    // IntelliJ IDEA suggests fixing it.
    System.out.printf("Hello and welcome!");

    // Press Ctrl+R or click the green arrow button in the gutter to run the code.
    for (int i = 1; i <= 5; i++) {

      // Press Ctrl+D to start debugging your code. We have set one breakpoint
      // for you, but you can always add more by pressing Cmd+F8.
      System.out.println("i = " + i);
    }
  }

  private static void init() {
    App.init("Git2Go");
    App.instance().loadProperties("config.properties");
  }

}
