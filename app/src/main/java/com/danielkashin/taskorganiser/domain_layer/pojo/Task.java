package com.danielkashin.taskorganiser.domain_layer.pojo;

import java.util.ArrayList;


public class Task {

  // ---------------------------------------- main info -------------------------------------------

  String UUID;

  Type type;

  String name;

  Long mainTimestamp;

  // ----------------------------------------- time -----------------------------------------------

  Long duration;

  Long minuteStart;

  Long minuteEnd;

  Long notificationTimestamp;

  // -------------------------------------- additional info ---------------------------------------

  String note;

  ArrayList<String> tags;

  ArrayList<Long> subtasks;

  Boolean done;

  // -------------------------------------- synchronization ---------------------------------------

  Boolean changedLocal;

  Boolean deletedLocal;

  Long changeOrDeleteLocalTimestamp;

  // ---------------------------------------- inner types -----------------------------------------

  public enum Type {
    Month,
    Week,
    Day,
    Mini
  }

}
