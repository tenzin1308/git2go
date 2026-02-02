package com.aexp.acq.go2.utils.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitHubLogger {

  private static final Logger log = LoggerFactory.getLogger(GitHubLogger.class);

  public static void info(String message) {
    log.info(message);
  }

  public static void error(String message) {
    log.error("::error::{}", message);
  }

}
