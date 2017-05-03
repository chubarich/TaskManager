package com.danielkashin.taskorganiser.data_layer.entities.local.data;

import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskDayContract;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.IdContract.COLUMN_NAME_ID;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_CHANGED_LOCAL;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_DATE;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_DATE;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_DELETED_LOCAL;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_DONE;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_DURATION;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_IMPORTANT;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_NAME;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_NOTE;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_UUID;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskDayContract.COLUMN_NAME_MINUTE_END;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskDayContract.COLUMN_NAME_MINUTE_START;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskDayContract.COLUMN_NAME_NOTIFICATION_DATE;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskDayContract.COLUMN_NAME_TASK_WEEK_ID;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskDayContract.TABLE_NAME;

@StorIOSQLiteType(table = TABLE_NAME)
public class TaskDay {

  // id

  @StorIOSQLiteColumn(name = COLUMN_NAME_ID, key = true)
  Long id;

  // TaskContract

  @StorIOSQLiteColumn(name = COLUMN_NAME_NAME)
  String name;

  @StorIOSQLiteColumn(name = COLUMN_NAME_DATE)
  String date;

  @StorIOSQLiteColumn(name = COLUMN_NAME_NOTE)
  String note;

  @StorIOSQLiteColumn(name = COLUMN_NAME_UUID)
  Long UUID;

  @StorIOSQLiteColumn(name = COLUMN_NAME_DURATION)
  Long duration;

  @StorIOSQLiteColumn(name = COLUMN_NAME_DONE)
  Integer done;

  @StorIOSQLiteColumn(name = COLUMN_NAME_IMPORTANT)
  Integer important;

  // TaskDayContract

  @StorIOSQLiteColumn(name = COLUMN_NAME_MINUTE_START)
  Long minuteStart;

  @StorIOSQLiteColumn(name = COLUMN_NAME_MINUTE_END)
  Long minuteEnd;

  @StorIOSQLiteColumn(name = COLUMN_NAME_NOTIFICATION_DATE)
  Integer notificationTimestamp;

  // connections

  @StorIOSQLiteColumn(name = COLUMN_NAME_TASK_WEEK_ID)
  Long taskWeekId;

  // synchronization

  @StorIOSQLiteColumn(name = COLUMN_NAME_CHANGED_LOCAL)
  Boolean changedLocal;

  @StorIOSQLiteColumn(name = COLUMN_NAME_DELETED_LOCAL)
  Boolean deletedLocal;

  @StorIOSQLiteColumn(name = COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_DATE)
  Long changeOrDeleteLocalTimestamp;


  TaskDay() {
  }

}
