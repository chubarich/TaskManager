package com.danielkashin.taskorganiser.data_layer.contracts.local;


public class TaskDayContract implements TaskContract {

  private TaskDayContract() {
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "table_name_task_day";

  public static final String COLUMN_NAME_MINUTE_START = "minute_start";

  public static final String COLUMN_NAME_MINUTE_END = "minute_end";


  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
      + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY, "
      + COLUMN_NAME_NAME + " TEXT NOT NULL, "
      + COLUMN_NAME_MAIN_TIMESTAMP + " INTEGER NOT NULL, "
      + COLUMN_NAME_NOTE + " TEXT, "
      + COLUMN_NAME_SUBTASKS + " INTEGER, "
      + COLUMN_NAME_TAGS + " INTEGER, "
      + COLUMN_NAME_DURATION + " INTEGER, "
      + COLUMN_NAME_MINUTE_START + " INTEGER, "
      + COLUMN_NAME_MINUTE_END + " INTEGER "
      + ");";

  public static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

  private static final String ESCAPE_CHAR = "#";


}
