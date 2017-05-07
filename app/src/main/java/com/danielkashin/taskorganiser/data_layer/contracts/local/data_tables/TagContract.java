package com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables;


import com.danielkashin.taskorganiser.data_layer.contracts.local.base.IdContract;

public class TagContract implements IdContract {

  private TagContract(){
  }

  // -------------------------------------- constants ---------------------------------------------

  public static final String TABLE_NAME = "tags";

  public static final String COLUMN_NAME_NAME = "name";

  public static final String SQL_CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
      // id
      + COLUMN_NAME_ID + " INTEGER NOT NULL, "

      // TagContract
      + COLUMN_NAME_NAME + " INTEGER NOT NULL UNIQUE ON CONFLICT REPLACE, "

      + "PRIMARY KEY (" + COLUMN_NAME_ID + ") "
      + ");";

  public static String SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

}
