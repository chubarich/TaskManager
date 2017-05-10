package com.danielkashin.taskorganiser.data_layer.contracts.local.connections_tables;


import com.danielkashin.taskorganiser.data_layer.contracts.local.base.IdContract;

public class TaskToTagContract implements IdContract {

  private TaskToTagContract(){
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "task_tag";

  public static final String COLUMN_NAME_TASK_UUID = "task_UUID";

  public static final String COLUMN_NAME_TAG_ID = "tag_id";

  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
      + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY, "
      + COLUMN_NAME_TASK_UUID + " INTEGER NOT NULL, "
      + COLUMN_NAME_TAG_ID + " INTEGER NOT NULL, "
      + "UNIQUE(" + COLUMN_NAME_TASK_UUID + ", " + COLUMN_NAME_TAG_ID + ") ON CONFLICT IGNORE"
      + ");";

  public static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;


}
