package com.danielkashin.taskorganiser.data_layer.entities.local.data;

import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskDayContract;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = TaskDayContract.TABLE_NAME)
public class TaskDay {

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_ID, key = true)
  Long id;

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_NAME)
  String name;

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_MAIN_TIMESTAMP)
  Long mainTimestamp;

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_NOTE)
  String note;

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_UUID)
  Long uniqueId;

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_DURATION)
  Long duration;

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_MINUTE_START)
  Long minuteStart;

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_MINUTE_END)
  Long minuteEnd;

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_CHANGED_LOCAL)
  Boolean changedLocal;

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_DELETED_LOCAL)
  Boolean deletedLocal;

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP)
  Long changeOrDeleteLocalTimestamp;

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_DONE)
  Integer done;

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_NOTIFICATION_TIMESTAMP)
  Integer notificationTimestamp;

}
