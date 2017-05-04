package com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables;


import com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract;

public class TaskWeekContract implements TaskContract {

  private TaskWeekContract() {
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "table_name_task_week";

  public static final String COLUMN_NAME_TASK_MONTH_ID = "task_month_id";


  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("

      // id
      + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY, "

      // TaskContract
      + COLUMN_NAME_NAME + " TEXT NOT NULL, "
      + COLUMN_NAME_DATE + " DATETIME NOT NULL, "
      + COLUMN_NAME_NOTE + " TEXT, "
      + COLUMN_NAME_UUID + " TEXT NOT NULL UNIQUE, "
      + COLUMN_NAME_DURATION + " INTEGER, "
      + COLUMN_NAME_DONE + " INTEGER, "
      + COLUMN_NAME_IMPORTANT + " INTEGER, "

      // synchronization
      + COLUMN_NAME_CHANGED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_DELETED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP + " TIMESTAMP, "

      // connections
      + COLUMN_NAME_TASK_MONTH_ID + " INTEGER, "

      + "PRIMARY KEY (" + COLUMN_NAME_ID + "), "
      + "FOREIGN KEY (" + COLUMN_NAME_TASK_MONTH_ID + ") REFERENCES "
      + TaskMonthContract.TABLE_NAME + "(" + TaskMonthContract.COLUMN_NAME_ID + ")"
      + ");";

  public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

  private static final String ESCAPE_CHAR = "#";

}
