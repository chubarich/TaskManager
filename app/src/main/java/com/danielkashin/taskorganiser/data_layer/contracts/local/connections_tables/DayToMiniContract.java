package com.danielkashin.taskorganiser.data_layer.contracts.local.connections_tables;


import com.danielkashin.taskorganiser.data_layer.contracts.local.base.IdContract;

public class DayToMiniContract implements IdContract {

  private DayToMiniContract(){
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "table_name_day_task_to_mini_task";

  public static final String COLUMN_NAME_DAY_ID = "day_id";

  public static final String COLUMN_NAME_MINI_TASK_ID = "local_id";

  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
      + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY, "
      + COLUMN_NAME_DAY_ID + " INTEGER NOT NULL, "
      + COLUMN_NAME_MINI_TASK_ID + " INTEGER NOT NULL, "
      + "UNIQUE(" + COLUMN_NAME_DAY_ID + ", " + COLUMN_NAME_MINI_TASK_ID + ") ON CONFLICT REPLACE"
      + ");";

  public static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

}
