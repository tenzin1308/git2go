package com.aexp.acq.go2.github_actions;

import com.aexp.acq.go2.base.App;
import com.aexp.acq.go2.base.BaseComponent;
import com.aexp.acq.go2.base.RestResponse;
import com.aexp.acq.go2.rest_interactions.CreatePullRequestReviewCommentEndPoint;
import com.aexp.acq.go2.rest_interactions.ListPullRequestFilesEndPoint;
import com.americanexpress.unify.jdocs.Document;
import com.americanexpress.unify.jdocs.JDocument;
import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CheckStyle extends BaseComponent {

  private static final Logger logger = LoggerFactory.getLogger(CheckStyle.class);
  private static final String checkStyleConfigUrl = App.instance().getProperty("checkstyle.config.url");
  private static final String checkStyleConfigPath = App.instance().getProperty("checkstyle.config.path");
  private final List<String> modifiedFiles = new ArrayList<>();
  private final Map<String, String> commitSHA = new HashMap<>();

  protected CheckStyle(String className) {
    super(className);
  }

  @Override
  protected Object process(Object... vargs) {
    try {
      if (Files.notExists(Paths.get(checkStyleConfigPath))) {
        logger.info("Checkstyle configuration file not found. Downloading from {}", checkStyleConfigUrl);
        try (InputStream inputStream = new URL(checkStyleConfigUrl).openStream()) {
          Files.copy(inputStream, Paths.get(checkStyleConfigPath), StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
          throw new RuntimeException(e);
        }
      }

      invokeListPullRequestFilesEndPoint();
      Checker checker = new Checker();
      checker.setModuleClassLoader(Checker.class.getClassLoader());
      Configuration configuration = ConfigurationLoader.loadConfiguration(checkStyleConfigPath, new PropertiesExpander(System.getProperties()));
      checker.configure(configuration);
      checker.addListener(new AuditListener() {
        @Override
        public void auditStarted(AuditEvent auditEvent) {

        }

        @Override
        public void auditFinished(AuditEvent auditEvent) {

        }

        @Override
        public void fileStarted(AuditEvent auditEvent) {

        }

        @Override
        public void fileFinished(AuditEvent auditEvent) {

        }

        @Override
        public void addError(AuditEvent auditEvent) {
          String fileName = auditEvent.getFileName();
          int line = auditEvent.getLine();
          String errorMessage = auditEvent.getMessage();
          String commitID = commitSHA.get(fileName);
          invokeCreatePullRequestReviewCommentEndPoint(fileName, commitID, line, errorMessage);
        }

        @Override
        public void addException(AuditEvent auditEvent, Throwable throwable) {

        }
      });
      List<File> filesToCheck = modifiedFiles.stream().map(File::new).toList();
      checker.process(filesToCheck);
      checker.destroy();
    }
    catch (Exception e) {
      logger.error("Error while running checkstyle -> {}", e.getMessage());
    }
    return null;
  }

  private void invokeListPullRequestFilesEndPoint() {
    ListPullRequestFilesEndPoint listPullRequestFilesEndPoint = new ListPullRequestFilesEndPoint("com.aexp.acq.go2.rest_interactions.ListPullRequestFilesEndPoint");
    RestResponse restResponse = (RestResponse)listPullRequestFilesEndPoint.execute(App.instance().getProperty("github.base.url"),
            App.instance().getProperty("github.repo"),
            App.instance().getProperty("checkstyle.pull.request.number"));
    // Parse the response
    Document responseDoc = new JDocument(restResponse.getResponseBody());
    for (int i = 0; i < responseDoc.getArraySize("$.[]"); i++) {
      if (responseDoc.getString("$.[%].filename", String.valueOf(i)).endsWith(".java")) {
        modifiedFiles.add(responseDoc.getString("$.[%].filename", String.valueOf(i)));
        commitSHA.put(responseDoc.getString("$.[%].filename", String.valueOf(i)), responseDoc.getString("$.[%].sha", String.valueOf(i)));
      }
    }
  }

  private void invokeCreatePullRequestReviewCommentEndPoint(String fileName, String commitSHA, int line, String errorMessage) {
    CreatePullRequestReviewCommentEndPoint createPullRequestReviewCommentEndPoint = new CreatePullRequestReviewCommentEndPoint("com.aexp.acq.go2.rest_interactions.CreatePullRequestReviewCommentEndPoint");
    createPullRequestReviewCommentEndPoint.execute(App.instance().getProperty("github.base.url"),
            App.instance().getProperty("github.repo"),
            App.instance().getProperty("checkstyle.pull.request.number"),
            "{\"body\":\"" + errorMessage + "\",\"commit_id\":\"" + commitSHA + "\",\"path\":\"" + fileName + "\",\"line\":" + line + ", \"side\":\"RIGHT\"}");
  }

}
