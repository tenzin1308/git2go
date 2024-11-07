package com.aexp.acq.go2.github_actions;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.BaseComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MavenCLI extends BaseComponent {

  private static final Logger logger = LoggerFactory.getLogger(MavenCLI.class);

  protected MavenCLI(String className) {
    super(className);
  }

  @Override
  protected Object process(Object... vargs) {
    List<String> mavenCommand = new ArrayList<>();
    mavenCommand.add("mvn");
    Arrays.stream((App.instance().getProperty("phases")).split(","))
            .map(String::trim)
            .forEach(mavenCommand::add);
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

  private boolean runMavenCommand(List<String> command) throws Exception {
    ProcessBuilder processBuilder = new ProcessBuilder(command);
    processBuilder.redirectErrorStream(true);
    Process process = processBuilder.start();
    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    String line;
    while ((line = bufferedReader.readLine()) != null) {
      System.out.println(line);
    }
    return process.waitFor() == 0;
  }

}
