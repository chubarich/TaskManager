package com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables;

import com.danielkashin.taskorganiser.data_layer.contracts.local.base.IdContract;
import com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract;


public class TaskNoDateContract implements IdContract, TaskContract {

  private TaskNoDateContract() {
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "no_date_tasks";


  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("

      // id
      + COLUMN_NAME_ID + " INTEGER NOT NULL PRIMARY KEY, "

      // TaskContract
      + COLUMN_NAME_NAME + " TEXT NOT NULL, "
      + COLUMN_NAME_NOTE + " TEXT, "
      + COLUMN_NAME_UUID + " TEXT NOT NULL UNIQUE ON CONFLICT REPLACE, "
      + COLUMN_NAME_DURATION + " INTEGER, "
      + COLUMN_NAME_DONE + " INTEGER, "
      + COLUMN_NAME_IMPORTANT + " INTEGER, "

      // synchronization
      + COLUMN_NAME_CHANGED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_DELETED_LOCAL + " INTEGER NOT NULL, "
      + COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP + " TIMESTAMP "

      + ");";

  public static final String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

}
