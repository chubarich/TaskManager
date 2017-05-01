package com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables;


import com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract;

public class TaskDayContract implements TaskContract {

  private TaskDayContract() {
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "table_name_task_day";

  public static final String COLUMN_NAME_MINUTE_START = "minute_start";

  public static final String COLUMN_NAME_MINUTE_END = "minute_end";

  public static final String COLUMN_NAME_NOTIFICATION_TIMESTAMP = "notification_timestamp";


  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
      + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY, "
      + COLUMN_NAME_NAME + " TEXT NOT NULL, "
      + COLUMN_NAME_MAIN_TIMESTAMP + " INTEGER NOT NULL, "
      + COLUMN_NAME_NOTE + " TEXT, "
      + COLUMN_NAME_UUID + " STRING NOT NULL UNIQUE, "
      + COLUMN_NAME_DURATION + " INTEGER, "
      + COLUMN_NAME_MINUTE_START + " INTEGER, "
      + COLUMN_NAME_MINUTE_END + " INTEGER, "
      + COLUMN_NAME_DONE + " INTEGER, "
      + COLUMN_NAME_NOTIFICATION_TIMESTAMP + " INTEGER, "
      // synchronization
      + COLUMN_NAME_CHANGED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_DELETED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP + " INTEGER "
      + ");";

  public static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

  private static final String ESCAPE_CHAR = "#";


}
