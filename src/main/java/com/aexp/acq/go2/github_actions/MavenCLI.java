package com.aexp.acq.go2.github_actions;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.BaseComponent;
import com.aexp.acq.go2.utils.BaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class MavenCLI extends BaseComponent {

  private static final Logger logger = LoggerFactory.getLogger(MavenCLI.class);

  protected MavenCLI(String className) {
    super(className);
  }

  @Override
  protected Object process(Object... vargs) {
    String mavenCommand = App.instance().getProperty("phases");
    if (BaseUtils.isNullOrEmpty(mavenCommand)) {
      // TODO: Decide wheather to have validation here or in validator class
    }
    try {
      boolean buildSuccess = runMavenCommand(mavenCommand);
      if (buildSuccess) {
        // Set the output
      }
      else {
        // set the error output
      }
    }
    catch (Exception e) {
      // set the Exception output
    }
    return null;
  }

  private boolean runMavenCommand(String command) throws Exception {
    Process process = Runtime.getRuntime().exec(command);
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      System.out.println(line);
    }
    process.waitFor();
    return process.exitValue() == 0;
  }

}
