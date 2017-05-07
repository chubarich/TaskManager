package com.danielkashin.taskorganiser.data_layer.services.local;

import com.danielkashin.taskorganiser.data_layer.entities.local.connections.TaskToTag;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.Tag;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskDay;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMini;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMonth;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskWeek;
import com.pushtorefresh.storio.sqlite.operations.get.PreparedGetListOfObjects;
import com.pushtorefresh.storio.sqlite.operations.get.PreparedGetObject;
import com.pushtorefresh.storio.sqlite.operations.put.PreparedPutObject;


public interface ITasksLocalService {

  // ---------------------------------------- get -------------------------------------------------

  PreparedGetListOfObjects<TaskWeek> getFavoriteWeekTasks();

  PreparedGetListOfObjects<TaskDay> getFavoriteDayTasks();

  PreparedGetListOfObjects<TaskMonth> getFavoriteMonthTasks();

  PreparedGetListOfObjects<TaskWeek> getWeekTasks(String date);

  PreparedGetListOfObjects<TaskDay> getDayTasks(String date);

  PreparedGetListOfObjects<TaskMonth> getMonthTasks(String date);

  PreparedGetListOfObjects<TaskToTag> getTaskToTags(String UUID);

  PreparedGetObject<Tag> getTag(long id);

  // ---------------------------------------- put -------------------------------------------------

  PreparedPutObject<TaskMonth> putMonthTask(TaskMonth monthTask);

  PreparedPutObject<TaskWeek> putWeekTask(TaskWeek weekTask);

  PreparedPutObject<TaskDay> putDayTask(TaskDay dayTask);

  PreparedPutObject<TaskMini> putMiniTask(TaskMini taskMini);


  // --------------------------------------- delete -----------------------------------------------

}
