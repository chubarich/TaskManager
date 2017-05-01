package com.danielkashin.taskorganiser.data_layer.entities.local.data;


import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskMiniContract;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = TaskMiniContract.TABLE_NAME)
public class TaskMini {

  @StorIOSQLiteColumn(name = TaskMiniContract.COLUMN_NAME_ID, key = true)
  Long id;

  @StorIOSQLiteColumn(name = TaskMiniContract.COLUMN_NAME_TEXT)
  String text;

  @StorIOSQLiteColumn(name = TaskMiniContract.COLUMN_NAME_DONE)
  Boolean done;


}
