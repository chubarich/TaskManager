package com.danielkashin.taskorganiser.data_layer.entities.local.connections;


import com.danielkashin.taskorganiser.data_layer.contracts.local.connections_tables.TaskToTagContract;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.IdContract.COLUMN_NAME_ID;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.connections_tables.TaskToTagContract.COLUMN_NAME_TAG_ID;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.connections_tables.TaskToTagContract.COLUMN_NAME_TASK_UUID;

@StorIOSQLiteType(table = TaskToTagContract.TABLE_NAME)
public class TaskToTag {

  @StorIOSQLiteColumn(name = COLUMN_NAME_ID)
  Long id;

  @StorIOSQLiteColumn(name = COLUMN_NAME_TASK_UUID)
  String taskUUID;

  @StorIOSQLiteColumn(name = COLUMN_NAME_TAG_ID)
  Long tagId;


  TaskToTag() {
  }




}
