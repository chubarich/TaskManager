package com.danielkashin.taskorganiser.domain_layer.helper;


public class NumbersHelper {

  public static Integer getInteger(Boolean bool) {
    return (bool == null || !bool) ? 0 : 1;
  }

}
