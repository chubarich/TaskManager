package com.danielkashin.taskorganiser.util;


public class NumbersHelper {

  public static Integer getInteger(Boolean bool) {
    return (bool == null || !bool) ? 0 : 1;
  }

}
