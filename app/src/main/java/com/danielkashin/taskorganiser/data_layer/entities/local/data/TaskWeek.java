package com.danielkashin.taskorganiser.data_layer.entities.local.data;


import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskWeekContract;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = TaskWeekContract.TABLE_NAME)
public class TaskWeek {

  @StorIOSQLiteColumn(name = TaskWeekContract.COLUMN_NAME_ID, key = true)
  Long id;

  @StorIOSQLiteColumn(name = TaskWeekContract.COLUMN_NAME_NAME)
  String name;

  @StorIOSQLiteColumn(name = TaskWeekContract.COLUMN_NAME_MAIN_TIMESTAMP)
  Long mainTimestamp;

  @StorIOSQLiteColumn(name = TaskWeekContract.COLUMN_NAME_NOTE)
  String note;

  @StorIOSQLiteColumn(name = TaskWeekContract.COLUMN_NAME_UUID)
  Long uniqueId;

  @StorIOSQLiteColumn(name = TaskWeekContract.COLUMN_NAME_DURATION)
  Long duration;

  @StorIOSQLiteColumn(name = TaskWeekContract.COLUMN_NAME_CHANGED_LOCAL)
  Boolean changedLocal;

  @StorIOSQLiteColumn(name = TaskWeekContract.COLUMN_NAME_DELETED_LOCAL)
  Boolean deletedLocal;

  @StorIOSQLiteColumn(name = TaskWeekContract.COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP)
  Long changeOrDeleteLocalTimestamp;

  @StorIOSQLiteColumn(name = TaskWeekContract.COLUMN_NAME_DONE)
  Integer done;


}
