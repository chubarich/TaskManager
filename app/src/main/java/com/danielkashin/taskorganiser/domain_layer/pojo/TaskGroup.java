package com.danielkashin.taskorganiser.domain_layer.pojo;


import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;

import java.util.ArrayList;

public class TaskGroup {

  private final String date;
  private final Task.Type type;
  private final ArrayList<Task> tasks;


  public TaskGroup(String date, Task.Type type) {
    this.date = date;
    this.type = type;
    this.tasks = new ArrayList<>();
  }

  // --------------------------------------- setters ----------------------------------------------

  public void setTask(Task task, int position) {
    boolean positionIsValid = position >= 0 && position < tasks.size();
    ExceptionHelper.assertTrue("Position is not valid", positionIsValid);

    tasks.set(position, task);
  }

  public int addTask(Task task) {
    ExceptionHelper.checkAllObjectsNonNull("Task must be non null", task);
    boolean taskIsValid = task.getDate().equals(this.date) && task.getType() == this.type;
    ExceptionHelper.assertTrue("Task is not valid", taskIsValid);

    if (type != Task.Type.Day) {
      tasks.add(task);
      return tasks.size() - 1;
    } else {
      boolean taskHasTime = task.getMinuteStart() != null;
      for (int i = 0; i < tasks.size(); ++i) {
        boolean currentTaskHasTime = tasks.get(i).getMinuteStart() != null;

        if (!taskHasTime && currentTaskHasTime) {
          tasks.add(i, task);
          return i;
        } else if (taskHasTime && currentTaskHasTime
            && tasks.get(i).getMinuteStart() > task.getMinuteStart()) {
          tasks.add(i, task);
          return i;
        }
      }

      tasks.add(task);
      return tasks.size() - 1;
    }
  }

  // --------------------------------------- getters ----------------------------------------------

  public Task.Type getType() {
    return type;
  }

  public String getDate() {
    return date;
  }

  public Task getTask(int position) {
    boolean positionIsValid = position >= 0 && position < tasks.size();
    ExceptionHelper.assertTrue("Position is not valid", positionIsValid);

    return tasks.get(position);
  }

  public int getTaskSize() {
    return tasks.size();
  }

  public Task popTask(int position) {
    boolean positionIsValid = position >= 0 && position < tasks.size();
    ExceptionHelper.assertTrue("Position is not valid", positionIsValid);

    return tasks.remove(position);
  }

}
