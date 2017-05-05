package com.danielkashin.taskorganiser.data_layer.services.local;

import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TagContract;
import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskDayContract;
import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskMonthContract;
import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskWeekContract;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.Tag;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskDay;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMonth;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskWeek;
import com.danielkashin.taskorganiser.data_layer.services.base.DatabaseService;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.get.PreparedGetListOfObjects;
import com.pushtorefresh.storio.sqlite.queries.Query;

public class TasksLocalService extends DatabaseService implements ITasksLocalService {

  private TasksLocalService(StorIOSQLite sqLite) {
    super(sqLite);
  }

  // -------------------------------- ITasksLocalService ------------------------------------------

  //                            ----------- delete -------------

  /*
  public PreparedDeleteObject<DatabaseTranslation> deleteTranslation(DatabaseTranslation translation) {
    return getSQLite().delete()
        .object(translation)
        .prepare();
  }
  */

  /*
  @Override
  public PreparedDeleteByQuery deleteNonFavoriteTranslations() {
    return getSQLite().delete()
        .byQuery(DeleteQuery.builder()
            .table(TranslationContract.TABLE_NAME)
            .where(TranslationContract.COLUMN_NAME_IS_FAVOURITE + " = " + 0)
            .build()
        ).prepare();
  }
  */

  //                           ------------ get ------------

  @Override
  public PreparedGetListOfObjects<TaskWeek> getWeekTasks(String date) {
    return getSQLite().get()
        .listOfObjects(TaskWeek.class)
        .withQuery(Query.builder()
            .table(TaskWeekContract.TABLE_NAME)
            .where(TaskWeekContract.COLUMN_NAME_DATE + " = \"" + date + "\"")
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<TaskDay> getDayTasks(String date) {
    return getSQLite().get()
        .listOfObjects(TaskDay.class)
        .withQuery(Query.builder()
            .table(TaskDayContract.TABLE_NAME)
            .where(TaskDayContract.COLUMN_NAME_DATE + " = \"" + date + "\"")
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<TaskMonth> getMonthTasks(String date) {
    return getSQLite().get()
        .listOfObjects(TaskMonth.class)
        .withQuery(Query.builder()
            .table(TaskMonthContract.TABLE_NAME)
            .where(TaskMonthContract.COLUMN_NAME_DATE + " = \"" + date + "\"")
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<Tag> getTags(String UUID) {
    return getSQLite().get()
        .listOfObjects(Tag.class)
        .withQuery(Query.builder()
            .table(TagContract.TABLE_NAME)
            .where(TagContract.COLUMN_NAME_TASK_UUID + " = \"" + UUID + "\"")
            .build())
        .prepare();
  }

  // ----------------------------------- inner types ----------------------------------------------

  public static class Factory {

    private Factory() {
    }

    public static ITasksLocalService create(StorIOSQLite sqLite) {
      return new TasksLocalService(sqLite);
    }

  }
}
