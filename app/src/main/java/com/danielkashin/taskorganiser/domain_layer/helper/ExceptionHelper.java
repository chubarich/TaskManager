package com.danielkashin.taskorganiser.domain_layer.helper;


public class ExceptionHelper {

  public static void checkAllObjectsNonNull(String exceptionMessage, Object... objects) {
    for (Object object : objects) {
      if (object == null) {
        throw new NullPointerException(exceptionMessage);
      }
    }
  }

  public static void assertFalse(String exceptionMessage, boolean condition) {
    if (condition) {
      throw new IllegalStateException(exceptionMessage);
    }
  }

  public static void assertTrue(String exceptionMessage, boolean condition) {
    if (!condition) {
      throw new IllegalStateException(exceptionMessage);
    }
  }

}
