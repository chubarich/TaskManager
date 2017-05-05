package com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables;


import com.danielkashin.taskorganiser.data_layer.contracts.local.base.IdContract;

public class TagContract implements IdContract {

  private TagContract(){
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "table_name_tag";

  public static final String COLUMN_NAME_TASK_UUID = "task_uuid";

  public static final String COLUMN_NAME_LABEL = "label";

  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
      // id
      + COLUMN_NAME_ID + " INTEGER NOT NULL, "

      // TagContract
      + COLUMN_NAME_TASK_UUID + " INTEGER NON NULL, "
      + COLUMN_NAME_LABEL + " INTEGER NOT NULL, "

      + "PRIMARY KEY (" + COLUMN_NAME_ID + ") "
      + ");";

  public static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

}
