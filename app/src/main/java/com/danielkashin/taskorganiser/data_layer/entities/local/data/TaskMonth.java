package com.danielkashin.taskorganiser.data_layer.entities.local.data;


import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskDayContract;
import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskMonthContract;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.IdContract.COLUMN_NAME_ID;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_DATE;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_DONE;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_DURATION;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_IMPORTANT;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_NAME;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_NOTE;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.TaskContract.COLUMN_NAME_UUID;

@StorIOSQLiteType(table = TaskMonthContract.TABLE_NAME)
public class TaskMonth {

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
  String UUID;

  @StorIOSQLiteColumn(name = COLUMN_NAME_DURATION)
  Long duration;

  @StorIOSQLiteColumn(name = COLUMN_NAME_DONE)
  Integer done;

  @StorIOSQLiteColumn(name = COLUMN_NAME_IMPORTANT)
  Integer important;

  // synchronization

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_CHANGED_LOCAL)
  Integer changedLocal;

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_DELETED_LOCAL)
  Integer deletedLocal;

  @StorIOSQLiteColumn(name = TaskDayContract.COLUMN_NAME_CHANGE_OR_DELETE_LOCAL_TIMESTAMP)
  Long changeOrDeleteLocalTimestamp;


  TaskMonth() {
  }

  public TaskMonth(Long id, String name, String date, String note, String UUID, Long duration,
                   Integer done, Integer important, Integer changedLocal, Integer deletedLocal,
                   Long changeOrDeleteLocalTimestamp) {
    this.id = id;
    this.name = name;
    this.date = date;
    this.note = note;
    this.UUID = UUID;
    this.duration = duration;
    this.done = done;
    this.important = important;
    this.changedLocal = changedLocal;
    this.deletedLocal = deletedLocal;
    this.changeOrDeleteLocalTimestamp = changeOrDeleteLocalTimestamp;
  }

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDate() {
    return date;
  }

  public String getNote() {
    return note;
  }

  public String getUUID() {
    return UUID;
  }

  public Long getDuration() {
    return duration;
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
