package com.danielkashin.taskorganiser.domain_layer.repository;

import com.danielkashin.taskorganiser.data_layer.entities.local.connections.TaskToTag;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.Tag;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskDay;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMini;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMonth;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskNoDate;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskWeek;
import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.util.DatetimeHelper;
import com.danielkashin.taskorganiser.util.NumbersHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.ImportantTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.TagTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.pushtorefresh.storio.sqlite.operations.delete.DeleteResult;
import com.pushtorefresh.storio.sqlite.operations.put.PutResult;

import java.util.ArrayList;
import java.util.List;


public class TasksRepository implements ITasksRepository {

  private ITasksLocalService tasksLocalService;


  private TasksRepository(ITasksLocalService tasksLocalService) {
    if (tasksLocalService == null) {
      throw new IllegalStateException("All repository arguments must be non null");
    }

    this.tasksLocalService = tasksLocalService;
  }

  // ---------------------------------------- public ----------------------------------------------

  //                            -------------- get ----------------

  @Override
  public ImportantTaskGroup getImportantData() throws ExceptionBundle {
    ImportantTaskGroup output = new ImportantTaskGroup();

    // setup month task group
    List<TaskMonth> monthTasks = tasksLocalService.getImportantMonthTasks()
        .executeAsBlocking();
    for (int i = 0; i < monthTasks.size(); ++i) {
      Task task = new Task(monthTasks.get(i));
      task.setTags(getTags(task.getUUID()));
      output.addTask(task);
    }

    // setup week task group
    List<TaskWeek> weekTasks = tasksLocalService.getImportantWeekTasks()
        .executeAsBlocking();
    for (TaskWeek taskWeek : weekTasks) {
      Task task = new Task(taskWeek);
      task.setTags(getTags(task.getUUID()));
      output.addTask(task);
    }

    // setup day task group
    List<TaskDay> dayTasks = tasksLocalService.getImportantDayTasks()
        .executeAsBlocking();
    for (TaskDay taskDay : dayTasks) {
      Task task = new Task(taskDay);
      task.setTags(getTags(task.getUUID()));
      output.addTask(task);
    }

    // setup no date task group
    List<TaskNoDate> noDateTasks = tasksLocalService.getImportantNoDateTasks()
        .executeAsBlocking();
    for (TaskNoDate taskNoDate : noDateTasks) {
      Task task = new Task(taskNoDate);
      task.setTags(getTags(task.getUUID()));
      output.addTask(task);
    }

    return output;
  }

  @Override
  public DateTypeTaskGroup getNoDateData() throws ExceptionBundle {
    List<TaskNoDate> rawNoDateTasks = tasksLocalService.getAllNoDateTasks()
        .executeAsBlocking();

    DateTypeTaskGroup output = new DateTypeTaskGroup(null, Task.Type.NoDate);
    for (TaskNoDate taskNoDate : rawNoDateTasks) {
      Task task = new Task(taskNoDate);
      task.setTags(getTags(task.getUUID()));
      output.addTask(task);
    }

    return output;
  }

  @Override
  public ArrayList<DateTypeTaskGroup> getDateTypeData(String date, Task.Type type) throws ExceptionBundle {
    if (type == Task.Type.Day) {
      return getWeekTasks(date);
    } else if (type == Task.Type.Week) {
      return getMonthTasks(date);
    } else if (type == Task.Type.Month) {
      return getYearTasks(date);
    } else {
      throw new IllegalStateException("Such task type is not supported");
    }
  }

  @Override
  public TagTaskGroup getTagTaskGroup(String tag) throws ExceptionBundle {
    TagTaskGroup output = new TagTaskGroup(tag);

    Tag rawTag = tasksLocalService.getTag(tag)
        .executeAsBlocking();

    if (rawTag != null) {
      List<TaskToTag> rawTaskToTags = tasksLocalService.getTaskToTags(rawTag.getId())
          .executeAsBlocking();

      if (rawTaskToTags.size() != 0) {
        String[] taskToTagsArray = new String[rawTaskToTags.size()];
        for (int i = 0; i < rawTaskToTags.size(); ++i) {
          taskToTagsArray[i] = rawTaskToTags.get(i).getTaskUUID();
        }

        // setup month task group
        List<TaskMonth> monthTasks = tasksLocalService.getMonthTasks(taskToTagsArray)
            .executeAsBlocking();
        for (int i = 0; i < monthTasks.size(); ++i) {
          Task task = new Task(monthTasks.get(i));
          task.setTags(getTags(task.getUUID()));
          output.addTask(task);
        }

        // setup week task group
        List<TaskWeek> weekTasks = tasksLocalService.getWeekTasks(taskToTagsArray)
            .executeAsBlocking();
        for (TaskWeek taskWeek : weekTasks) {
          Task task = new Task(taskWeek);
          task.setTags(getTags(task.getUUID()));
          output.addTask(task);
        }

        // setup day task group
        List<TaskDay> dayTasks = tasksLocalService.getDayTasks(taskToTagsArray)
            .executeAsBlocking();
        for (TaskDay taskDay : dayTasks) {
          Task task = new Task(taskDay);
          task.setTags(getTags(task.getUUID()));
          output.addTask(task);
        }

        // setup no date task group
        List<TaskNoDate> noDateTasks = tasksLocalService.getNoDateTasks(taskToTagsArray)
            .executeAsBlocking();
        for (TaskNoDate taskNoDate : noDateTasks) {
          Task task = new Task(taskNoDate);
          task.setTags(getTags(task.getUUID()));
          output.addTask(task);
        }
      }
    }

    return output;
  }

  @Override
  public ArrayList<String> getTags() throws ExceptionBundle {
    ArrayList<String> tags = new ArrayList<>();

    List<Tag> rawTags = tasksLocalService.getTags()
        .executeAsBlocking();

    for (Tag tag : rawTags) {
      tags.add(tag.getName());
    }

    return tags;
  }

  //                                 ----------- save ------------

  @Override
  public void saveTask(Task task) throws ExceptionBundle {
    saveTags(task);

    if (task.getType() == Task.Type.NoDate) {
      TaskNoDate taskNoDate = new TaskNoDate(null, task.getName(), task.getNote(), task.getUUID(),
          NumbersHelper.getInteger(task.getDone()),
          NumbersHelper.getInteger(task.getImportant()), 1, 0,
          DatetimeHelper.getCurrentTimestamp());

      tasksLocalService.putNoDateTask(taskNoDate)
          .executeAsBlocking();
    } else if (task.getType() == Task.Type.Day) {
      TaskDay taskDay = new TaskDay(null, task.getName(), task.getDate(), task.getNote(), task.getUUID(),
          task.getDuration(), NumbersHelper.getInteger(task.getDone()),
          NumbersHelper.getInteger(task.getImportant()), task.getMinuteStart(),
          task.getMinuteEnd(), task.getNotificationTimestamp(), null, 1, 0,
          DatetimeHelper.getCurrentTimestamp());

      tasksLocalService.putDayTask(taskDay)
          .executeAsBlocking();
    } else if (task.getType() == Task.Type.Week) {
      TaskWeek taskWeek = new TaskWeek(null, task.getName(), task.getDate(), task.getNote(),
          task.getUUID(), task.getDuration(), NumbersHelper.getInteger(task.getDone()),
          NumbersHelper.getInteger(task.getImportant()), null, 1, 0,
          DatetimeHelper.getCurrentTimestamp());

      tasksLocalService.putWeekTask(taskWeek)
          .executeAsBlocking();
    } else if (task.getType() == Task.Type.Month) {
      TaskMonth taskMonth = new TaskMonth(null, task.getName(), task.getDate(), task.getNote(),
          task.getUUID(), task.getDuration(), NumbersHelper.getInteger(task.getDone()),
          NumbersHelper.getInteger(task.getImportant()), 1, 0,
          DatetimeHelper.getCurrentTimestamp());

      tasksLocalService.putMonthTask(taskMonth)
          .executeAsBlocking();
    } else if (task.getType() == Task.Type.Mini) {
      TaskMini miniTask = new TaskMini(null, task.getName(), NumbersHelper.getInteger(task.getDone()),
          task.getUUID(), null, 1, 0, DatetimeHelper.getCurrentTimestamp());

      tasksLocalService.putMiniTask(miniTask)
          .executeAsBlocking();
    } else {
      throw new IllegalStateException("Such task type is not supported");
    }
  }

  @Override
  public void saveTag(String name) throws ExceptionBundle {
    Tag rawTag = new Tag(name);

    PutResult putResult = tasksLocalService.putTag(rawTag)
        .executeAsBlocking();

    if (putResult.wasNotInserted()) {
      throw new ExceptionBundle(ExceptionBundle.Reason.PUT_DENIED);
    }
  }

  // ----------------------------------------- delete ---------------------------------------------

  @Override
  public void deleteTag(String tagName) {
    Tag tag = tasksLocalService.getTag(tagName)
        .executeAsBlocking();

    if (tag != null) {
      tasksLocalService.deleteTag(tagName)
          .executeAsBlocking();
      tasksLocalService.deleteTaskToTag(tag.getId())
          .executeAsBlocking();
    }
  }

  // ---------------------------------------- private ---------------------------------------------

  private ArrayList<DateTypeTaskGroup> getWeekTasks(String date) throws ExceptionBundle {
    // get the needed dates
    ArrayList<String> allDaysOfWeek = DatetimeHelper.getAllDaysOfWeek(date);

    // setup output dates and types
    ArrayList<DateTypeTaskGroup> output = new ArrayList<>();
    output.add(new DateTypeTaskGroup(allDaysOfWeek.get(0), Task.Type.Week));
    for (int i = 0; i < allDaysOfWeek.size(); ++i) {
      output.add(new DateTypeTaskGroup(allDaysOfWeek.get(i), Task.Type.Day));
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

  private ArrayList<DateTypeTaskGroup> getMonthTasks(String date) throws ExceptionBundle {
    // get the needed dates
    ArrayList<String> firstDaysOfWeeks = DatetimeHelper.getFirstDaysOfWeeksInMonth(date);

    // setup output dates and types
    ArrayList<DateTypeTaskGroup> output = new ArrayList<>();
    output.add(new DateTypeTaskGroup(DatetimeHelper.getFirstDayOfMonth(date), Task.Type.Month));
    for (int i = 0; i < firstDaysOfWeeks.size(); ++i) {
      output.add(new DateTypeTaskGroup(firstDaysOfWeeks.get(i), Task.Type.Week));
    }

    // setup month task group
    List<TaskMonth> monthTasks = tasksLocalService.getMonthTasks(date)
        .executeAsBlocking();
    for (int i = 0; i < monthTasks.size(); ++i) {
      Task task = new Task(monthTasks.get(i));
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
        output.get(i + 1).addTask(task);
      }
    }

    return output;
  }

  private ArrayList<DateTypeTaskGroup> getYearTasks(String date) throws ExceptionBundle {
    // get the needed dates
    ArrayList<String> months = DatetimeHelper.getAllMonthsInYear(date);

    // setup output dates and types
    ArrayList<DateTypeTaskGroup> output = new ArrayList<>();
    for (int i = 0; i < months.size(); ++i) {
      output.add(new DateTypeTaskGroup(months.get(i), Task.Type.Month));
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

  private void saveTags(Task task) {
    ArrayList<String> tagNames = task.getTags();

    if (tagNames != null) {
      ArrayList<Long> tagIds = new ArrayList<>();
      for (String tagName : tagNames) {
        Tag tag = tasksLocalService.getTag(tagName)
            .executeAsBlocking();
        if (tag != null) {
          tagIds.add(tag.getId());
        } else {
          PutResult putResult = tasksLocalService.putTag(new Tag(tagName))
              .executeAsBlocking();
          if (putResult.insertedId() != null) {
            tagIds.add(putResult.insertedId());
          }
        }
      }

      for (Long tagId : tagIds) {
        tasksLocalService.putTaskToTag(new TaskToTag(task.getUUID(), tagId))
            .executeAsBlocking();
      }
    }
  }

  private ArrayList<String> getTags(String UUID) {
    List<TaskToTag> rawTags = tasksLocalService.getTaskToTags(UUID)
        .executeAsBlocking();

    ArrayList<String> tags = new ArrayList<>();
    if (rawTags.size() != 0) {
      for (TaskToTag taskToTag : rawTags) {
        Tag tag = tasksLocalService.getTag(taskToTag.getTagId())
            .executeAsBlocking();
        if (tag != null) {
          tags.add(tag.getName());
        }
      }
    }

    return tags;
  }


  public static class Factory {

    private Factory() {
    }


    public static ITasksRepository create(ITasksLocalService tasksLocalService) {
      return new TasksRepository(tasksLocalService);
    }
  }
}
