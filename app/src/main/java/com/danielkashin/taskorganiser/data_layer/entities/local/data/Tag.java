package com.danielkashin.taskorganiser.data_layer.entities.local.data;


import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TagContract;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

import static com.danielkashin.taskorganiser.data_layer.contracts.local.base.IdContract.COLUMN_NAME_ID;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TagContract.COLUMN_NAME_LABEL;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TagContract.COLUMN_NAME_TASK_UUID;
import static com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TagContract.TABLE_NAME;

@StorIOSQLiteType(table = TABLE_NAME)
public class Tag {

  @StorIOSQLiteColumn(name = COLUMN_NAME_ID, key = true)
  Long id;

  @StorIOSQLiteColumn(name = COLUMN_NAME_TASK_UUID)
  String UUID;

  @StorIOSQLiteColumn(name = COLUMN_NAME_LABEL)
  String label;


  Tag() {
  }

  public String getLabel() {
    return label;
  }

}
