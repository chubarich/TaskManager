package com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables;


import com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract;


public class TaskMonthContract implements TaskContract {

  private TaskMonthContract() {
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "table_name_task_month";

  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("

      // id
      + COLUMN_NAME_ID + " INTEGER NOT NULL, "

      // TaskContract
      + COLUMN_NAME_NAME + " TEXT NOT NULL, "
      + COLUMN_NAME_DATE + " DATETIME NOT NULL, "
      + COLUMN_NAME_NOTE + " TEXT, "
      + COLUMN_NAME_UUID + " TEXT NOT NULL UNIQUE, "
      + COLUMN_NAME_DURATION + " INTEGER, "
      + COLUMN_NAME_DONE + " INTEGER NOT NULL, "
      + COLUMN_NAME_IMPORTANT + " INTEGER NOT NULL, "

      // synchronization
      + COLUMN_NAME_CHANGED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_DELETED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP + " TIMESTAMP, "

      + "PRIMARY KEY (" + COLUMN_NAME_ID + ") "
      + ");";

  public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

  private static final String ESCAPE_CHAR = "#";

}
