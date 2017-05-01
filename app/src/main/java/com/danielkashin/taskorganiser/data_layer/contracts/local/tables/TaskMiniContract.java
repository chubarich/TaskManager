package com.danielkashin.taskorganiser.data_layer.contracts.local.tables;


import com.danielkashin.taskorganiser.data_layer.contracts.local.base.IdContract;

public class TaskMiniContract implements IdContract {

  private TaskMiniContract() {
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "table_name_task_mini";

  public static final String COLUMN_NAME_TEXT = "text";

  public static final String COLUMN_NAME_DONE = "done";

  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
      + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY, "
      + COLUMN_NAME_TEXT + " TEXT NOT NULL, "
      + COLUMN_NAME_DONE + " INTEGER NOT NULL "
      + ");";

  public static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

  private static final String ESCAPE_CHAR = "#";


}
