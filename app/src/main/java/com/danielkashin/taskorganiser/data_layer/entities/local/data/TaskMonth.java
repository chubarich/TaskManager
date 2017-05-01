package com.danielkashin.taskorganiser.data_layer.entities.local.data;


import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskMonthContract;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = TaskMonthContract.TABLE_NAME)
public class TaskMonth {

  @StorIOSQLiteColumn(name = TaskMonthContract.COLUMN_NAME_ID, key = true)
  Long id;

  @StorIOSQLiteColumn(name = TaskMonthContract.COLUMN_NAME_NAME)
  String name;

  @StorIOSQLiteColumn(name = TaskMonthContract.COLUMN_NAME_MAIN_TIMESTAMP)
  Long mainTimestamp;

  @StorIOSQLiteColumn(name = TaskMonthContract.COLUMN_NAME_NOTE)
  String note;

  @StorIOSQLiteColumn(name = TaskMonthContract.COLUMN_NAME_UUID)
  Long uniqueId;

  @StorIOSQLiteColumn(name = TaskMonthContract.COLUMN_NAME_DURATION)
  Long duration;

  @StorIOSQLiteColumn(name = TaskMonthContract.COLUMN_NAME_CHANGED_LOCAL)
  Boolean changedLocal;

  @StorIOSQLiteColumn(name = TaskMonthContract.COLUMN_NAME_DELETED_LOCAL)
  Boolean deletedLocal;

  @StorIOSQLiteColumn(name = TaskMonthContract.COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP)
  Long changeOrDeleteLocalTimestamp;

  @StorIOSQLiteColumn(name = TaskMonthContract.COLUMN_NAME_DONE)
  Integer done;


}
