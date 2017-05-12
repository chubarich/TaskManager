package com.danielkashin.taskorganiser.data_layer.services.local;

import com.danielkashin.taskorganiser.data_layer.entities.local.connections.TaskToTag;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.Tag;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskDay;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMini;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMonth;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskNoDate;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskWeek;
import com.pushtorefresh.storio.sqlite.operations.delete.PreparedDeleteByQuery;
import com.pushtorefresh.storio.sqlite.operations.delete.PreparedDeleteObject;
import com.pushtorefresh.storio.sqlite.operations.get.PreparedGet;
import com.pushtorefresh.storio.sqlite.operations.get.PreparedGetListOfObjects;
import com.pushtorefresh.storio.sqlite.operations.get.PreparedGetObject;
import com.pushtorefresh.storio.sqlite.operations.put.PreparedPutObject;


public interface ITasksLocalService {

  // ---------------------------------------- get -------------------------------------------------

  PreparedGetListOfObjects<Tag> getTags();

  PreparedGetObject<Tag> getTag(long id);

  PreparedGetObject<Tag> getTag(String tag);

  PreparedGetListOfObjects<TaskToTag> getTaskToTags(String UUID);

  PreparedGetListOfObjects<TaskToTag> getTaskToTags(Long tagId);


  PreparedGetListOfObjects<TaskWeek> getImportantWeekTasks();

  PreparedGetListOfObjects<TaskDay> getImportantDayTasks();

  PreparedGetListOfObjects<TaskMonth> getImportantMonthTasks();

  PreparedGetListOfObjects<TaskNoDate> getImportantNoDateTasks();


  PreparedGetListOfObjects<TaskWeek> getDoneWeekTasks();

  PreparedGetListOfObjects<TaskDay> getDoneDayTasks();

  PreparedGetListOfObjects<TaskMonth> getDoneMonthTasks();

  PreparedGetListOfObjects<TaskNoDate> getDoneNoDateTasks();


  PreparedGetListOfObjects<TaskWeek> getWeekTasks(String[] UUIDs);

  PreparedGetListOfObjects<TaskDay> getDayTasks(String[] UUIDs);

  PreparedGetListOfObjects<TaskNoDate> getNoDateTasks(String[] UUIDs);

  PreparedGetListOfObjects<TaskMonth> getMonthTasks(String[] UUIDs);


  PreparedGetListOfObjects<TaskWeek> getWeekTasks(String date);

  PreparedGetListOfObjects<TaskDay> getDayTasks(String date);

  PreparedGetListOfObjects<TaskMonth> getMonthTasks(String date);


  PreparedGetListOfObjects<TaskNoDate> getAllNoDateTasks();

  // ---------------------------------------- put -------------------------------------------------

  PreparedPutObject<TaskMonth> putMonthTask(TaskMonth monthTask);

  PreparedPutObject<TaskWeek> putWeekTask(TaskWeek weekTask);

  PreparedPutObject<TaskDay> putDayTask(TaskDay dayTask);

  PreparedPutObject<TaskMini> putMiniTask(TaskMini taskMini);

  PreparedPutObject<TaskNoDate> putNoDateTask(TaskNoDate taskNoDate);

  PreparedPutObject<Tag> putTag(Tag tag);

  PreparedPutObject<TaskToTag> putTaskToTag(TaskToTag taskToTag);

  // --------------------------------------- delete -----------------------------------------------

  PreparedDeleteByQuery deleteTaskToTag(Long tagId);

  PreparedDeleteByQuery deleteTag(String tagName);

}
