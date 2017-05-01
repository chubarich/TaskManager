package com.danielkashin.taskorganiser.data_layer.contracts.local;


public class TaskMonthContract implements TaskContract {

  private TaskMonthContract() {
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "table_name_task_month";

  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
      + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY, "
      + COLUMN_NAME_NAME + " TEXT NOT NULL, "
      + COLUMN_NAME_MAIN_TIMESTAMP + " INTEGER NOT NULL, "
      + COLUMN_NAME_NOTE + " TEXT, "
      + COLUMN_NAME_SUBTASKS + " INTEGER, "
      + COLUMN_NAME_TAGS + " TEXT, "
      + COLUMN_NAME_DURATION + " INTEGER "
      + ");";

  public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

  private static final String ESCAPE_CHAR = "#";

}
