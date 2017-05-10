package com.danielkashin.taskorganiser.data_layer.services.local;

import com.danielkashin.taskorganiser.data_layer.contracts.local.connections_tables.TaskToTagContract;
import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TagContract;
import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskDayContract;
import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskMonthContract;
import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskNoDateContract;
import com.danielkashin.taskorganiser.data_layer.contracts.local.data_tables.TaskWeekContract;
import com.danielkashin.taskorganiser.data_layer.entities.local.connections.TaskToTag;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.Tag;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskDay;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMini;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMonth;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskNoDate;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskWeek;
import com.danielkashin.taskorganiser.data_layer.services.base.DatabaseService;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.pushtorefresh.storio.sqlite.StorIOSQLite;
import com.pushtorefresh.storio.sqlite.operations.delete.PreparedDeleteByQuery;
import com.pushtorefresh.storio.sqlite.operations.delete.PreparedDeleteObject;
import com.pushtorefresh.storio.sqlite.operations.get.PreparedGetListOfObjects;
import com.pushtorefresh.storio.sqlite.operations.get.PreparedGetObject;
import com.pushtorefresh.storio.sqlite.operations.put.PreparedPutObject;
import com.pushtorefresh.storio.sqlite.queries.DeleteQuery;
import com.pushtorefresh.storio.sqlite.queries.Query;

public class TasksLocalService extends DatabaseService implements ITasksLocalService {

  private TasksLocalService(StorIOSQLite sqLite) {
    super(sqLite);
  }

  // -------------------------------- ITasksLocalService ------------------------------------------

  //                           ------------ get ------------

  @Override
  public PreparedGetListOfObjects<Tag> getTags() {
    return getSQLite().get()
        .listOfObjects(Tag.class)
        .withQuery(Query.builder()
            .table(TagContract.TABLE_NAME)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<TaskToTag> getTaskToTags(String UUID) {
    return getSQLite().get()
        .listOfObjects(TaskToTag.class)
        .withQuery(Query.builder()
            .table(TaskToTagContract.TABLE_NAME)
            .where(TaskToTagContract.COLUMN_NAME_TASK_UUID + " = ?")
            .whereArgs(UUID)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<TaskToTag> getTaskToTags(Long tagId) {
    return getSQLite().get()
        .listOfObjects(TaskToTag.class)
        .withQuery(Query.builder()
            .table(TaskToTagContract.TABLE_NAME)
            .where(TaskToTagContract.COLUMN_NAME_TAG_ID + " = ?")
            .whereArgs(tagId)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetObject<Tag> getTag(long id) {
    return getSQLite().get()
        .object(Tag.class)
        .withQuery(Query.builder()
            .table(TagContract.TABLE_NAME)
            .where(TagContract.COLUMN_NAME_ID + " = ?")
            .whereArgs(id)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetObject<Tag> getTag(String tag) {
    return getSQLite().get()
        .object(Tag.class)
        .withQuery(Query.builder()
            .table(TagContract.TABLE_NAME)
            .where(TagContract.COLUMN_NAME_NAME + " = ?")
            .whereArgs(tag)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<TaskWeek> getImportantWeekTasks() {
    return getSQLite().get()
        .listOfObjects(TaskWeek.class)
        .withQuery(Query.builder()
            .table(TaskWeekContract.TABLE_NAME)
            .where(TaskWeekContract.COLUMN_NAME_IMPORTANT + " = \"" + 1 + "\""
                + " AND " + TaskWeekContract.COLUMN_NAME_DELETED_LOCAL + " = " + 0)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<TaskDay> getImportantDayTasks() {
    return getSQLite().get()
        .listOfObjects(TaskDay.class)
        .withQuery(Query.builder()
            .table(TaskDayContract.TABLE_NAME)
            .where(TaskDayContract.COLUMN_NAME_IMPORTANT + " = \"" + 1 + "\""
                + " AND " + TaskDayContract.COLUMN_NAME_DELETED_LOCAL + " = " + 0)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<TaskMonth> getImportantMonthTasks() {
    return getSQLite().get()
        .listOfObjects(TaskMonth.class)
        .withQuery(Query.builder()
            .table(TaskMonthContract.TABLE_NAME)
            .where(TaskMonthContract.COLUMN_NAME_IMPORTANT + " = \"" + 1 + "\""
                + " AND " + TaskMonthContract.COLUMN_NAME_DELETED_LOCAL + " = " + 0)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<TaskNoDate> getImportantNoDateTasks() {
    return getSQLite().get()
        .listOfObjects(TaskNoDate.class)
        .withQuery(Query.builder()
            .table(TaskNoDateContract.TABLE_NAME)
            .where(TaskNoDateContract.COLUMN_NAME_IMPORTANT + " = ? AND "
                + TaskNoDateContract.COLUMN_NAME_DELETED_LOCAL + " = ?")
            .whereArgs(1, 0)
            .build())
        .prepare();
  }


  @Override
  public PreparedGetListOfObjects<TaskWeek> getWeekTasks(String[] UUIDs) {
    return getSQLite().get()
        .listOfObjects(TaskWeek.class)
        .withQuery(Query.builder()
            .table(TaskWeekContract.TABLE_NAME)
            .where(TaskWeekContract.COLUMN_NAME_DELETED_LOCAL + " = 0 AND "
                + TaskWeekContract.COLUMN_NAME_UUID + generateSearchIn(UUIDs.length))
            .whereArgs(UUIDs)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<TaskDay> getDayTasks(String[] UUIDs) {
    return getSQLite().get()
        .listOfObjects(TaskDay.class)
        .withQuery(Query.builder()
            .table(TaskDayContract.TABLE_NAME)
            .where(TaskDayContract.COLUMN_NAME_DELETED_LOCAL + " = 0 AND "
                + TaskDayContract.COLUMN_NAME_UUID + generateSearchIn(UUIDs.length))
            .whereArgs(UUIDs)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<TaskNoDate> getNoDateTasks(String[] UUIDs) {
    return getSQLite().get()
        .listOfObjects(TaskNoDate.class)
        .withQuery(Query.builder()
            .table(TaskNoDateContract.TABLE_NAME)
            .where(TaskNoDateContract.COLUMN_NAME_DELETED_LOCAL + " = 0 AND "
                + TaskNoDateContract.COLUMN_NAME_UUID + generateSearchIn(UUIDs.length))
            .whereArgs(UUIDs)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<TaskMonth> getMonthTasks(String[] UUIDs) {
    return getSQLite().get()
        .listOfObjects(TaskMonth.class)
        .withQuery(Query.builder()
            .table(TaskMonthContract.TABLE_NAME)
            .where(TaskMonthContract.COLUMN_NAME_DELETED_LOCAL + " = 0 AND "
                + TaskMonthContract.COLUMN_NAME_UUID + generateSearchIn(UUIDs.length))
            .whereArgs(UUIDs)
            .build())
        .prepare();
  }


  @Override
  public PreparedGetListOfObjects<TaskWeek> getWeekTasks(String date) {
    return getSQLite().get()
        .listOfObjects(TaskWeek.class)
        .withQuery(Query.builder()
            .table(TaskWeekContract.TABLE_NAME)
            .where(TaskWeekContract.COLUMN_NAME_DATE + " = \"" + date + "\""
                + " AND " + TaskWeekContract.COLUMN_NAME_DELETED_LOCAL + " = " + 0)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<TaskDay> getDayTasks(String date) {
    return getSQLite().get()
        .listOfObjects(TaskDay.class)
        .withQuery(Query.builder()
            .table(TaskDayContract.TABLE_NAME)
            .where(TaskDayContract.COLUMN_NAME_DATE + " = \"" + date + "\""
                + " AND " + TaskDayContract.COLUMN_NAME_DELETED_LOCAL + " = " + 0)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<TaskMonth> getMonthTasks(String date) {
    return getSQLite().get()
        .listOfObjects(TaskMonth.class)
        .withQuery(Query.builder()
            .table(TaskMonthContract.TABLE_NAME)
            .where(TaskMonthContract.COLUMN_NAME_DATE + " = \"" + date + "\""
                + " AND " + TaskMonthContract.COLUMN_NAME_DELETED_LOCAL + " = " + 0)
            .build())
        .prepare();
  }

  @Override
  public PreparedGetListOfObjects<TaskNoDate> getAllNoDateTasks() {
    return getSQLite().get()
        .listOfObjects(TaskNoDate.class)
        .withQuery(Query.builder()
            .table(TaskNoDateContract.TABLE_NAME)
            .build())
        .prepare();
  }

  //                           ------------ put ------------

  @Override
  public PreparedPutObject<TaskMonth> putMonthTask(TaskMonth monthTask) {
    return getSQLite().put()
        .object(monthTask)
        .prepare();
  }

  @Override
  public PreparedPutObject<TaskWeek> putWeekTask(TaskWeek weekTask) {
    return getSQLite().put()
        .object(weekTask)
        .prepare();
  }

  @Override
  public PreparedPutObject<TaskDay> putDayTask(TaskDay dayTask) {
    return getSQLite().put()
        .object(dayTask)
        .prepare();
  }

  @Override
  public PreparedPutObject<TaskMini> putMiniTask(TaskMini taskMini) {
    return getSQLite().put()
        .object(taskMini)
        .prepare();
  }

  @Override
  public PreparedPutObject<Tag> putTag(Tag tag) {
    return getSQLite().put()
        .object(tag)
        .prepare();
  }

  @Override
  public PreparedPutObject<TaskToTag> putTaskToTag(TaskToTag taskToTag) {
    return getSQLite().put()
        .object(taskToTag)
        .prepare();
  }

  @Override
  public PreparedPutObject<TaskNoDate> putNoDateTask(TaskNoDate taskNoDate) {
    return getSQLite().put()
        .object(taskNoDate)
        .prepare();
  }

  // ------------------------------------- delete -------------------------------------------------

  @Override
  public PreparedDeleteByQuery deleteTaskToTag(Long tagId) {
    return getSQLite().delete()
        .byQuery(DeleteQuery.builder()
            .table(TaskToTagContract.TABLE_NAME)
            .where(TaskToTagContract.COLUMN_NAME_TAG_ID + " = ?")
            .whereArgs(tagId)
            .build())
        .prepare();
  }

  @Override
  public PreparedDeleteByQuery deleteTag(String tagName) {
    return getSQLite().delete()
        .byQuery(DeleteQuery.builder()
            .table(TagContract.TABLE_NAME)
            .where(TagContract.COLUMN_NAME_NAME + " = ?")
            .whereArgs(tagName)
            .build())
        .prepare();
  }

// ------------------------------------- private ------------------------------------------------

  private String generateSearchIn(int count) {
    ExceptionHelper.assertTrue("", count > 0);

    if (count == 1) {
      return " = ? ";
    }

    String result = " IN (";
    for (int i = 0; i < count; ++i) {
      result += i == 0 ? "?" : ", ?";
    }
    result += ")";

    return result;
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
