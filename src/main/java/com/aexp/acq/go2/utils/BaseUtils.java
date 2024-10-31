package com.aexp.acq.go2.utils;

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
   * Get the simple class name of a class
   *
   * @param clazz
   * @return the simple class name
   */
  public static String getSimpleClassName(Class<?> clazz) {
    return clazz.getSimpleName();
  }

}
