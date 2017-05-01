package com.danielkashin.taskorganiser.data_layer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.danielkashin.taskorganiser.BuildConfig;
import com.danielkashin.taskorganiser.data_layer.contracts.local.tables.TaskDayContract;
import com.danielkashin.taskorganiser.data_layer.contracts.local.tables.TaskMonthContract;
import com.danielkashin.taskorganiser.data_layer.contracts.local.tables.TaskWeekContract;


public class DatabaseOpenHelper extends SQLiteOpenHelper {

  public DatabaseOpenHelper(Context context) {
    super(context, BuildConfig.DATABASE_NAME, null, BuildConfig.DATABASE_BUILD_NUMBER);
  }

  @Override
  public void onCreate(SQLiteDatabase sqLiteDatabase) {
    sqLiteDatabase.execSQL(TaskDayContract.SQL_CREATE_TABLE);
    sqLiteDatabase.execSQL(TaskWeekContract.SQL_CREATE_TABLE);
    sqLiteDatabase.execSQL(TaskMonthContract.SQL_CREATE_TABLE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    sqLiteDatabase.execSQL(TaskDayContract.SQL_DELETE_TABLE);
    sqLiteDatabase.execSQL(TaskWeekContract.SQL_DELETE_TABLE);
    sqLiteDatabase.execSQL(TaskMonthContract.SQL_DELETE_TABLE);
    onCreate(sqLiteDatabase);
  }
}
