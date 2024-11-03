package com.aexp.acq.go2.github_actions;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.BaseComponent;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import com.puppycrawl.tools.checkstyle.api.Violation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckStyle extends BaseComponent {

  private static final Logger logger = LoggerFactory.getLogger(CheckStyle.class);
  private static final String checkStyleConfigUrl = App.instance().getProperty("checkstyle.config.url");
  private static final String checkStyleConfigPath = App.instance().getProperty("checkstyle.config.path");

  protected CheckStyle(String className) {
    super(className);
  }

  @Override
  protected Object process(Object... vargs) {
    // download the checkstyle config file
    try {
      if(Files.notExists(Paths.get(checkStyleConfigPath))){
        logger.info("Downloading the checkstyle config file from {}", checkStyleConfigUrl);
        try (InputStream inputStream = new URL(checkStyleConfigUrl).openStream()) {
          Files.copy(inputStream, Paths.get(checkStyleConfigPath), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
      Configuration config = ConfigurationLoader.loadConfiguration(checkStyleConfigPath,null, true);
    }
    catch (RuntimeException e) {
      throw new RuntimeException(e);
    }
    return null;
  }

  // TODO: implement the get changed files from a PR

  private List<Violation> runCheckStyle(List<String> files) throws IOException, CheckstyleException {
    List<Violation> violations = new ArrayList<>();

    Configuration config = ConfigurationLoader.loadConfiguration(checkStyleConfigPath, new Properties);
    Checker checker = new Checker();

    checker.setModuleClassLoader(CheckStyle.class.getClassLoader());
    checker.configure(config);

    for (String file : files) {
      checker.process(new File(file));
    }
    checker.destroy();

    for (String file: files) {
      violations.addAll()
    }

  }

  private List<Violation> getViolationFromFile(String filePath) throws IOException {
    List<Violation> violations = new ArrayList<>();
    String command = "java -jar checkstyle-*.jar -c " +
            checkStyleConfigPath + " " +
            filePath;
    ProcessBuilder processBuilder= new ProcessBuilder(command.split(" "));
    processBuilder.redirectErrorStream(true);
    Process process = processBuilder.start();

    try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
      String line;
      Pattern violationPattern = Pattern.compile("^(.*):\\s*(\\d+)\\s*:.*?\\s*(.*)$");
      while ((line = bufferedReader.readLine()) != null){
        Matcher matcher = violationPattern.matcher(line);
        if (matcher.matches()) {
          String fileName = matcher.group(1);
          int lineNumber = Integer.parseInt(matcher.group(2));
          String message = matcher.group(3);
          violations.add(new Violation(lineNumber, message));
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return violations;
  }



}
