package com.danielkashin.taskorganiser.data_layer.entities.local.data;


import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskDayContract;
import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskMiniContract;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskMiniContract.COLUMN_NAME_CHANGED_LOCAL;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskMiniContract.COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskMiniContract.COLUMN_NAME_DELETED_LOCAL;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskMiniContract.COLUMN_NAME_UUID;

@StorIOSQLiteType(table = TaskMiniContract.TABLE_NAME)
public class TaskMini {

  // id
  @StorIOSQLiteColumn(name = TaskMiniContract.COLUMN_NAME_ID, key = true)
  Long id;

  // TaskMiniContract

  @StorIOSQLiteColumn(name = TaskMiniContract.COLUMN_NAME_TEXT)
  String text;

  @StorIOSQLiteColumn(name = TaskMiniContract.COLUMN_NAME_DONE)
  Integer done;

  @StorIOSQLiteColumn(name = COLUMN_NAME_UUID)
  String UUID;

  // connections

  @StorIOSQLiteColumn(name = TaskMiniContract.COLUMN_NAME_TASK_DAY_ID)
  Long taskDayId;

  // synchronization

  @StorIOSQLiteColumn(name = COLUMN_NAME_CHANGED_LOCAL)
  Integer changedLocal;

  @StorIOSQLiteColumn(name = COLUMN_NAME_DELETED_LOCAL)
  Integer deletedLocal;

  @StorIOSQLiteColumn(name = COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP)
  Long changeOrDeleteLocalTimestamp;


  TaskMini(){
  }

  public TaskMini(Long id, String text, Integer done, String UUID, Long taskDayId,
                  Integer changedLocal, Integer deletedLocal, Long changeOrDeleteLocalTimestamp) {
    this.id = id;
    this.text = text;
    this.done = done;
    this.UUID = UUID;
    this.taskDayId = taskDayId;
    this.changedLocal = changedLocal;
    this.deletedLocal = deletedLocal;
    this.changeOrDeleteLocalTimestamp = changeOrDeleteLocalTimestamp;
  }
}
