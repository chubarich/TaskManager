package com.danielkashin.taskorganiser.data_layer.services.local;

import com.danielkashin.taskorganiser.data_layer.entities.local.data.Tag;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskDay;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMonth;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskWeek;
import com.pushtorefresh.storio.sqlite.operations.get.PreparedGetListOfObjects;


public interface ITasksLocalService {

  PreparedGetListOfObjects<TaskWeek> getWeekTasks(String date);

  PreparedGetListOfObjects<TaskDay> getDayTasks(String date);

  PreparedGetListOfObjects<TaskMonth> getMonthTasks(String date);

  PreparedGetListOfObjects<Tag> getTags(String UUID);

}
