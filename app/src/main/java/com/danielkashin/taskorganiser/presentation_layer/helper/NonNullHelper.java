package com.danielkashin.taskorganiser.presentation_layer.helper;


public class NonNullHelper {

  public static void checkAllObjectsNonNull(String exceptionMessage, Object... objects) {
    for (Object object : objects) {
      if (object == null) {
        throw new IllegalStateException(exceptionMessage);
      }
    }
  }

}
