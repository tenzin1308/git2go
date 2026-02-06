package com.aexp.acq.go2.utils.logging;

import org.slf4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.UUID;

public final class ActionLogger {

  private ActionLogger() {
  }

  public static void startGroup(Logger log, String message) {
    log.info("::group::{}", message);
  }

  public static void endGroup(Logger log) {
    log.info("::endgroup::");
  }

  public static void notice(Logger log, String message) {
    log.info("::notice::{}", message);
  }

  public static void mask(Logger log, String secret) {
    if (secret == null || secret.isBlank()) {
      return;
    }
    log.info("::add-mask::{}", secret);
  }

  public static void setOutput(Logger log, String name, String value) {
    String path = System.getProperty("GITHUB_OUTPUT");
    if (path == null || path.isBlank()) {
      return;
    }
    appendKeyValueFile(Path.of(path), name, value);
  }

  public static void addEnv(Logger log, String name, String value) {
    String path = System.getProperty("GITHUB_ENV");
    if (path == null || path.isBlank()) {
      return;
    }
    appendKeyValueFile(Path.of(path), name, value);
  }

  private static void appendKeyValueFile(Path file, String name, String value) {
    Objects.requireNonNull(name, "name");
    if (name.isBlank()) {
      throw new IllegalArgumentException("Name must not be blank.");
    }
    String val = (value == null) ? "" : value;
    String delim = "EOF_" + UUID.randomUUID();
    String payload = name + "<<" + delim + "\n" + val + "\n" + delim + "\n";

    try {
      Files.writeString(file, payload, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
    }
    catch (IOException e) {
      throw new RuntimeException("Failed writing to GitHub file command: " + file, e);
    }
  }

}
