package com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables;


import com.danielkashin.taskorganiser.data_layer.contracts.local.base.IdContract;

public class TaskMiniContract implements IdContract {

  private TaskMiniContract() {
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "table_name_task_mini";

  public static final String COLUMN_NAME_TEXT = "text";

  public static final String COLUMN_NAME_DONE = "done";

  public static final String COLUMN_NAME_UUID = "uuid";

  public static final String COLUMN_NAME_CHANGED_LOCAL = "changed_local";

  public static final String COLUMN_NAME_DELETED_LOCAL = "deleted_local";

  public static final String COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP = "change_or_delete_local_timestamp";

  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
      + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY, "
      + COLUMN_NAME_UUID + " STRING NOT NULL UNIQUE, "
      + COLUMN_NAME_TEXT + " TEXT NOT NULL, "
      + COLUMN_NAME_DONE + " INTEGER NOT NULL "
      // synchronization
      + COLUMN_NAME_CHANGED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_DELETED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP + " INTEGER "
      + ");";

  public static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

  private static final String ESCAPE_CHAR = "#";


}
