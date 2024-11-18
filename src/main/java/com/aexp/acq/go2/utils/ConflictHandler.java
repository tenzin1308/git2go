package com.aexp.acq.go2.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class ConflictHandler {

  private static final Logger logger = LoggerFactory.getLogger(ConflictHandler.class);
  private final String conflictResolution;
  private final Map<String, String> conflictLog;

  public ConflictHandler(String conflictResolution) {
    this.conflictResolution = conflictResolution;
    this.conflictLog = new HashMap<>();
  }

  public String resolveConflict(String fileName, String ours, String theirs) {
    String resolution;
    if ("latest".equalsIgnoreCase(conflictResolution)) {
      // logic for the latest resolution
      resolution = ours.compareTo(theirs) > 0 ? ours : theirs;
    }
    else if ("ours".equalsIgnoreCase(conflictResolution)) {
      resolution = ours;
    }
    else {
      resolution = theirs;
    }
    conflictLog.put(fileName, resolution);
    return resolution;
  }

  public Map<String, String> getConflictLog() {
    return conflictLog;
  }

}
