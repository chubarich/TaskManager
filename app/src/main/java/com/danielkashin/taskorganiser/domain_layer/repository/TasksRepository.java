package com.danielkashin.taskorganiser.domain_layer.repository;


import android.icu.util.Calendar;

import com.danielkashin.taskorganiser.data_layer.entities.local.data.Tag;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskDay;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMonth;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskWeek;
import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.domain_layer.helper.DatetimeHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.pojo.TaskGroup;

import java.util.ArrayList;
import java.util.List;

public class TasksRepository implements ITasksRepository {

  private ITasksLocalService tasksLocalService;

  public TasksRepository(ITasksLocalService tasksLocalService) {
    if (tasksLocalService == null) {
      throw new IllegalStateException("All repository arguments must be non null");
    }

    this.tasksLocalService = tasksLocalService;
  }

  @Override
  public ArrayList<TaskGroup> getWeekData(String date) throws ExceptionBundle {
    // get the needed dates
    ArrayList<String> allDaysOfWeek = DatetimeHelper.getAllDaysOfWeek(date);

    // setup output dates and types
    ArrayList<TaskGroup> output = new ArrayList<>();
    output.add(new TaskGroup(allDaysOfWeek.get(0), Task.Type.Week));
    for (int i = 0; i < allDaysOfWeek.size(); ++i) {
      output.add(new TaskGroup(allDaysOfWeek.get(i), Task.Type.Day));
    }

    // setup week task group
    List<TaskWeek> weekTasks = tasksLocalService.getWeekTasks(allDaysOfWeek.get(0))
        .executeAsBlocking();
    for (TaskWeek taskWeek : weekTasks) {
      Task task = new Task(taskWeek);
      task.setTags(getTags(task.getUUID()));
      output.get(0).addTask(task);
    }

    // setup days task group
    for (int i = 0; i < allDaysOfWeek.size(); ++i) {
      List<TaskDay> dayTasks = tasksLocalService.getDayTasks(allDaysOfWeek.get(i))
          .executeAsBlocking();

      for (TaskDay taskDay : dayTasks) {
        Task task = new Task(taskDay);
        task.setTags(getTags(task.getUUID()));
        output.get(i + 1).addTask(task);
      }
    }

    return output;
  }

  @Override
  public ArrayList<TaskGroup> getMonthData(String date) throws ExceptionBundle {
    // get the needed dates
    ArrayList<String> firstDaysOfWeeks = DatetimeHelper.getFirstDaysOfWeeksInMonth(date);

    // setup output dates and types
    ArrayList<TaskGroup> output = new ArrayList<>();
    output.add(new TaskGroup(DatetimeHelper.getFirstDayOfMonth(date), Task.Type.Month));
    for (int i = 0; i < firstDaysOfWeeks.size(); ++i) {
      output.add(new TaskGroup(firstDaysOfWeeks.get(i), Task.Type.Week));
    }

    // setup month task group
    List<TaskMonth> monthTasks = tasksLocalService.getMonthTasks(date)
        .executeAsBlocking();
    for (TaskMonth taskMonth : monthTasks) {
      Task task = new Task(taskMonth);
      task.setTags(getTags(task.getUUID()));
      output.get(0).addTask(task);
    }

    // setup weeks task group
    for (int i = 0; i < firstDaysOfWeeks.size(); ++i) {
      List<TaskWeek> weekTasks = tasksLocalService.getWeekTasks(firstDaysOfWeeks.get(i))
          .executeAsBlocking();
      for (TaskWeek taskWeek : weekTasks) {
        Task task = new Task(taskWeek);
        task.setTags(getTags(task.getUUID()));
        output.get(i).addTask(task);
      }
    }

    return output;
  }

  @Override
  public ArrayList<TaskGroup> getYearData(String date) throws ExceptionBundle {
    // get the needed dates
    ArrayList<String> months = DatetimeHelper.getAllMonthsInYear(date);

    // setup output dates and types
    ArrayList<TaskGroup> output = new ArrayList<>();
    for (int i = 0; i < months.size(); ++i) {
      output.add(new TaskGroup(months.get(i), Task.Type.Month));
    }

    // setup weeks task group
    for (int i = 0; i < months.size(); ++i) {
      List<TaskMonth> monthTasks = tasksLocalService.getMonthTasks(months.get(i))
          .executeAsBlocking();
      for (TaskMonth month : monthTasks) {
        Task task = new Task(month);
        task.setTags(getTags(task.getUUID()));
        output.get(i).addTask(task);
      }
    }

    return output;
  }


  private ArrayList<String> getTags(String UUID) {
    List<Tag> rawTags = tasksLocalService.getTags(UUID).executeAsBlocking();

    if (rawTags.size() != 0) {
      ArrayList<String> tags = new ArrayList<>();
      for (Tag tag : rawTags) {
        tags.add(tag.getLabel());
      }
      return tags;
    } else {
      return null;
    }
  }
}
