package com.danielkashin.taskorganiser.data_layer.entities.local.data;


import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TagContract;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteColumn;
import com.pushtorefresh.storio.sqlite.annotations.StorIOSQLiteType;

@StorIOSQLiteType(table = TagContract.TABLE_NAME)
public class Tag {

  @StorIOSQLiteColumn(name = TagContract.COLUMN_NAME_ID, key = true)
  Long id;

  @StorIOSQLiteColumn(name = TagContract.COLUMN_NAME_LABEL)
  String label;

}
