package com.danielkashin.taskorganiser.data_layer.entities.local.data;


import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskDayContract;
import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskNoDateContract;
import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskWeekContract;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.IdContract.COLUMN_NAME_ID;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskDateContract.COLUMN_NAME_DATE;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskDateContract.COLUMN_NAME_DURATION;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskNoDateContract.COLUMN_NAME_CHANGED_LOCAL;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskNoDateContract.COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskNoDateContract.COLUMN_NAME_DELETED_LOCAL;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskNoDateContract.COLUMN_NAME_DONE;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskNoDateContract.COLUMN_NAME_IMPORTANT;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskNoDateContract.COLUMN_NAME_NAME;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskNoDateContract.COLUMN_NAME_NOTE;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskNoDateContract.COLUMN_NAME_UUID;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskWeekContract.COLUMN_NAME_TASK_MONTH_ID;


@StorIOSQLiteType(table = TaskNoDateContract.TABLE_NAME)
public class TaskNoDate {

  // id

  @StorIOSQLiteColumn(name = COLUMN_NAME_ID, key = true)
  Long id;


  @StorIOSQLiteColumn(name = COLUMN_NAME_NAME)
  String name;

  @StorIOSQLiteColumn(name = COLUMN_NAME_NOTE)
  String note;

  @StorIOSQLiteColumn(name = COLUMN_NAME_UUID)
  String UUID;

  @StorIOSQLiteColumn(name = COLUMN_NAME_DONE)
  Integer done;

  @StorIOSQLiteColumn(name = COLUMN_NAME_IMPORTANT)
  Integer important;

  // synchronization

  @StorIOSQLiteColumn(name = COLUMN_NAME_CHANGED_LOCAL)
  Integer changedLocal;

  @StorIOSQLiteColumn(name = COLUMN_NAME_DELETED_LOCAL)
  Integer deletedLocal;

  @StorIOSQLiteColumn(name = COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP)
  Long changeOrDeleteLocalTimestamp;


  TaskNoDate() {
  }

  public TaskNoDate(Long id, String name, String note, String UUID, Integer done, Integer important,
                    Integer changedLocal, Integer deletedLocal, Long changeOrDeleteLocalTimestamp) {
    this.id = id;
    this.name = name;
    this.note = note;
    this.UUID = UUID;
    this.done = done;
    this.important = important;
    this.changedLocal = changedLocal;
    this.deletedLocal = deletedLocal;
    this.changeOrDeleteLocalTimestamp = changeOrDeleteLocalTimestamp;
  }

  public void setDeletedLocal(boolean deletedLocal) {
    this.deletedLocal = deletedLocal ? 1 : 0;
  }

  public void setChangeOrDeleteLocalTimestamp(Long timestamp) {
    this.changeOrDeleteLocalTimestamp = timestamp;
  }


  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getNote() {
    return note;
  }

  public String getUUID() {
    return UUID;
  }

  public Integer getDone() {
    return done;
  }

  public Integer getImportant() {
    return important;
  }

  public Integer getChangedLocal() {
    return changedLocal;
  }

  public Integer getDeletedLocal() {
    return deletedLocal;
  }

  public Long getChangeOrDeleteLocalTimestamp() {
    return changeOrDeleteLocalTimestamp;
  }

}