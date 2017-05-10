package com.danielkashin.taskorganiser.data_layer.database;

import android.content.Context;

import com.danielkashin.taskorganiser.data_layer.entities.local.connections.TaskToTag;
import com.danielkashin.taskorganiser.data_layer.entities.local.connections.TaskToTagStorIOSQLiteDeleteResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.connections.TaskToTagStorIOSQLiteGetResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.connections.TaskToTagStorIOSQLitePutResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.Tag;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TagStorIOSQLiteDeleteResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TagStorIOSQLiteGetResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TagStorIOSQLitePutResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskDay;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskDayStorIOSQLiteDeleteResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskDayStorIOSQLiteGetResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskDayStorIOSQLitePutResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMini;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMiniStorIOSQLiteDeleteResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMiniStorIOSQLiteGetResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMiniStorIOSQLitePutResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMonth;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMonthStorIOSQLiteDeleteResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMonthStorIOSQLiteGetResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMonthStorIOSQLitePutResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskNoDate;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskNoDateStorIOSQLiteDeleteResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskNoDateStorIOSQLiteGetResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskNoDateStorIOSQLitePutResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskWeek;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskWeekStorIOSQLiteDeleteResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskWeekStorIOSQLiteGetResolver;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskWeekStorIOSQLitePutResolver;
import com.pushtorefresh.storio.sqlite.SQLiteTypeMapping;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite;


public class SQLiteFactory {

  private SQLiteFactory() {
  }

  public static StorIOSQLite create(Context context) {
    return DefaultStorIOSQLite.builder()
        .sqliteOpenHelper(new DatabaseOpenHelper(context))
        .addTypeMapping(TaskMonth.class, SQLiteTypeMapping.<TaskMonth>builder()
            .putResolver(new TaskMonthStorIOSQLitePutResolver())
            .getResolver(new TaskMonthStorIOSQLiteGetResolver())
            .deleteResolver(new TaskMonthStorIOSQLiteDeleteResolver())
            .build())
        .addTypeMapping(TaskWeek.class, SQLiteTypeMapping.<TaskWeek>builder()
            .putResolver(new TaskWeekStorIOSQLitePutResolver())
            .getResolver(new TaskWeekStorIOSQLiteGetResolver())
            .deleteResolver(new TaskWeekStorIOSQLiteDeleteResolver())
            .build())
        .addTypeMapping(TaskDay.class, SQLiteTypeMapping.<TaskDay>builder()
            .putResolver(new TaskDayStorIOSQLitePutResolver())
            .getResolver(new TaskDayStorIOSQLiteGetResolver())
            .deleteResolver(new TaskDayStorIOSQLiteDeleteResolver())
            .build())
        .addTypeMapping(TaskMini.class, SQLiteTypeMapping.<TaskMini>builder()
            .putResolver(new TaskMiniStorIOSQLitePutResolver())
            .getResolver(new TaskMiniStorIOSQLiteGetResolver())
            .deleteResolver(new TaskMiniStorIOSQLiteDeleteResolver())
            .build())
        .addTypeMapping(TaskNoDate.class, SQLiteTypeMapping.<TaskNoDate>builder()
            .putResolver(new TaskNoDateStorIOSQLitePutResolver())
            .getResolver(new TaskNoDateStorIOSQLiteGetResolver())
            .deleteResolver(new TaskNoDateStorIOSQLiteDeleteResolver())
            .build())
        .addTypeMapping(Tag.class, SQLiteTypeMapping.<Tag>builder()
            .putResolver(new TagStorIOSQLitePutResolver())
            .getResolver(new TagStorIOSQLiteGetResolver())
            .deleteResolver(new TagStorIOSQLiteDeleteResolver())
            .build())
        .addTypeMapping(TaskToTag.class, SQLiteTypeMapping.<TaskToTag>builder()
            .putResolver(new TaskToTagStorIOSQLitePutResolver())
            .getResolver(new TaskToTagStorIOSQLiteGetResolver())
            .deleteResolver(new TaskToTagStorIOSQLiteDeleteResolver())
            .build())
        .build();
  }

}
