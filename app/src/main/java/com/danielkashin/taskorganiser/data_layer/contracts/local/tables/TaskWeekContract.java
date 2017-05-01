package com.danielkashin.taskorganiser.data_layer.contracts.local.tables;


import com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract;

public class TaskWeekContract implements TaskContract {

  private TaskWeekContract() {
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "table_name_task_week";

  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
      + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY, "
      + COLUMN_NAME_NAME + " TEXT NOT NULL, "
      + COLUMN_NAME_MAIN_TIMESTAMP + " INTEGER NOT NULL, "
      + COLUMN_NAME_NOTE + " TEXT, "
      + COLUMN_NAME_UNIQUE_ID + " INTEGER NOT NULL UNIQUE, "
      + COLUMN_NAME_DURATION + " INTEGER, "
      // synchronization
      + COLUMN_NAME_CHANGED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_DELETED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP + " INTEGER "
      + ");";

  public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

  private static final String ESCAPE_CHAR = "#";

}
