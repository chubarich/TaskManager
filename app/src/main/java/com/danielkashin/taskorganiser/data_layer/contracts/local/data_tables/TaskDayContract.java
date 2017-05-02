package com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables;

import com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract;


public class TaskDayContract implements TaskContract {

  private TaskDayContract() {
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "table_name_task_day";

  public static final String COLUMN_NAME_TASK_WEEK_ID = "task_week_id";

  public static final String COLUMN_NAME_MINUTE_START = "minute_start";

  public static final String COLUMN_NAME_MINUTE_END = "minute_end";

  public static final String COLUMN_NAME_NOTIFICATION_DATE = "notification_timestamp";


  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
      // id
      + COLUMN_NAME_ID + " INTEGER NOT NULL, "

      // TaskContract
      + COLUMN_NAME_NAME + " TEXT NOT NULL, "
      + COLUMN_NAME_DATE + " DATETIME NOT NULL, "
      + COLUMN_NAME_NOTE + " TEXT, "
      + COLUMN_NAME_UUID + " TEXT NOT NULL UNIQUE, "
      + COLUMN_NAME_DURATION + " INTEGER, "
      + COLUMN_NAME_DONE + " INTEGER, "
      + COLUMN_NAME_IMPORTANT + " INTEGER, "

      // TaskDayContract
      + COLUMN_NAME_MINUTE_START + " INTEGER, "
      + COLUMN_NAME_MINUTE_END + " INTEGER, "
      + COLUMN_NAME_NOTIFICATION_DATE + " DATETIME, "

      // connections
      + COLUMN_NAME_TASK_WEEK_ID + " INTEGER, "

      // synchronization
      + COLUMN_NAME_CHANGED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_DELETED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_DATE + " DATETIME, "

      + "PRIMARY KEY (" + COLUMN_NAME_ID + "), "
      + "FOREIGN KEY (" + COLUMN_NAME_TASK_WEEK_ID + ") REFERENCES "
      + TaskWeekContract.TABLE_NAME + "(" + TaskWeekContract.COLUMN_NAME_ID + ")"
      + ");";

  public static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

  private static final String ESCAPE_CHAR = "#";


}
