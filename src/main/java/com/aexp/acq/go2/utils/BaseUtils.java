package com.aexp.acq.go2.utils;

import com.aexp.acq.go2.base.App;

import java.io.InputStream;

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

}
