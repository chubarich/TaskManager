package com.danielkashin.taskorganiser.data_layer.contracts.local.additional_tables;


import com.danielkashin.taskorganiser.data_layer.contracts.local.base.IdContract;

public class MonthTaskToWeekTaskContract implements IdContract {

  private MonthTaskToWeekTaskContract(){
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "table_name_month_task_to_week_task";

  public static final String COLUMN_NAME_MONTH_ID = "day_id";

  public static final String COLUMN_NAME_WEEK_ID = "week_id";

  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
      + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY, "
      + COLUMN_NAME_MONTH_ID + " INTEGER NOT NULL, "
      + COLUMN_NAME_WEEK_ID + " INTEGER NOT NULL, "
      + "UNIQUE(" + COLUMN_NAME_MONTH_ID + ", " + COLUMN_NAME_WEEK_ID + ") ON CONFLICT REPLACE"
      + ");";

  public static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

}
