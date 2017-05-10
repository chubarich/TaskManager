package com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables;


import com.danielkashin.taskorganiser.data_layer.contracts.local.base.IdContract;

public class TaskNoDateContract implements IdContract {

  private TaskNoDateContract() {
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "no_date_tasks";

  public static final String COLUMN_NAME_NAME = "name";

  public static final String COLUMN_NAME_NOTE = "note";

  public static final String COLUMN_NAME_DONE = "done";

  public static final String COLUMN_NAME_IMPORTANT = "important";

  public static final String COLUMN_NAME_UUID = "uuid";

  public static final String COLUMN_NAME_CHANGED_LOCAL = "changed_local";

  public static final String COLUMN_NAME_DELETED_LOCAL = "deleted_local";

  public static final String COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP = "change_or_delete_date";


  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("

      // id
      + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY, "

      // TaskDateContract
      + COLUMN_NAME_NAME + " TEXT NOT NULL, "
      + COLUMN_NAME_NOTE + " TEXT, "
      + COLUMN_NAME_UUID + " TEXT NOT NULL UNIQUE ON CONFLICT REPLACE, "
      + COLUMN_NAME_DONE + " INTEGER, "
      + COLUMN_NAME_IMPORTANT + " INTEGER, "

      // synchronization
      + COLUMN_NAME_CHANGED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_DELETED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP + " TIMESTAMP "

      + ");";

  public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

}
