package com.aexp.acq.go2.utils;

import com.aexp.acq.go2.base.App;

import java.io.InputStream;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;

public class BaseUtils {

  /**
   * Check if a string is null or empty
   *
   * @param str
   * @return true if the string is null or empty
   */
  public static boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

  /**
   * Check if a string is not (null and empty)
   *
   * @param str
   * @return true if the string is not (null and empty)
   */
  public static boolean isNotNullOrEmpty(String str) {
    return str != null && str.trim().isEmpty() == false;
  }

  /**
   * Get the simple class name of a class
   *
   * @param className
   * @return the simple class name
   */
  public static String getSimpleClassName(String className) {
    return className.substring(className.lastIndexOf('.') + 1);
  }

  /**
   * Get the simple class name of a class
   *
   * @param clazz
   * @return the simple class name
   */
  public static String getSimpleClassName(Class<?> clazz) {
    return clazz.getSimpleName();
  }

  /**
   * Get the canonical name of a class
   *
   * @param clazz
   * @return the canonical name
   */
  public static String getCanonicalName(Class<?> clazz) {
    return clazz.getCanonicalName();
  }

  /**
   * Get the canonical name of a class
   *
   * @param className
   * @return the canonical name
   */
  public static String getCanonicalName(String className) {
    return className;
  }

  /**
   * Get the resource as a string
   *
   * @param clazz
   * @param filePath
   * @return the resource as a string
   */
  public static String getResourceAsString(Class<App> clazz, String filePath) {
    try (InputStream inputStream = clazz.getClassLoader().getResourceAsStream(filePath)) {
      byte[] bytes = new byte[inputStream.available()];
      inputStream.read(bytes);
      return new String(bytes);
    }
    catch (Exception e) {
      throw new RuntimeException("Error reading resource as string", e);
    }
  }

  /**
   * Check if a branch is excluded
   *
   * @param string
   * @return true if the branch is excluded
   */
  public static boolean isBranchExcluded(String string) {
    String[] token = getEmptyWhenNull(App.instance().getProperty("github.excluded.branches")).split(",");
    for (String s : token) {
      if (s.trim().equals(string)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Get an empty string when the input is null
   *
   * @param string
   * @return the input string or an empty string if the input is null
   */
  public static String getEmptyWhenNull(String string) {
    return string == null ? "" : string;
  }

  /**
   * Check if a branch is stale
   *
   * @param updatedAt
   * @param threshold
   * @return true if the branch is stale
   */
  public static boolean isStale(String updatedAt, String threshold, String inPattern) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(inPattern);
    ZonedDateTime updatedDate = ZonedDateTime.parse(updatedAt, formatter);
    ZonedDateTime now = ZonedDateTime.now();
    long daysDifference = ChronoUnit.DAYS.between(updatedDate, now);
    int thresholdDays = Integer.parseInt(threshold);
    return daysDifference > thresholdDays;
  }

  /**
   * Substitute tokens in a string
   *
   * @param input
   * @param tokens
   * @return the input string with tokens substituted
   */
  public static String stringSubstitutor(String input, Map<String, String> tokens) {
    if (input != null && tokens != null) {
      for (Map.Entry<String, String> entry : tokens.entrySet()) {
        String pattern = "${" + entry.getKey() + "}";
        input = input.replace(pattern, entry.getValue());
      }
    }
    return input;
  }

}
