package com.aexp.acq.go2.utils;

public class BaseUtils {

  /**
   * Check if a string is null or empty
   * @param str
   * @return true if the string is null or empty
   */
  public static boolean isNullOrEmpty(String str) {
    return str == null || str.trim().isEmpty();
  }

}
