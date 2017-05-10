package com.danielkashin.taskorganiser.domain_layer.pojo;

import com.danielkashin.taskorganiser.util.ExceptionHelper;

import java.util.ArrayList;
import java.util.Collections;


public class DateTypeTaskGroup implements ITaskGroup {

  private String date;
  private Task.Type type;
  private final ArrayList<Task> tasks;


  public DateTypeTaskGroup(String date, Task.Type type) {
    this.date = date;
    this.type = type;
    this.tasks = new ArrayList<>();
  }

  public DateTypeTaskGroup(DateTypeTaskGroup taskGroup) {
    tasks = new ArrayList<>();
    initialize(taskGroup);
  }

  // --------------------------------------- setters ----------------------------------------------


  @Override
  public boolean canBelongTo(Task task) {
    return task.getType() == type && (task.getDate() == null && date == null || task.getDate().equals(date));
  }

  @Override
  public void initialize(ITaskGroup taskGroup) {
    ExceptionHelper.assertTrue("", taskGroup instanceof DateTypeTaskGroup);

    date = ((DateTypeTaskGroup) taskGroup).getDate();
    type = ((DateTypeTaskGroup) taskGroup).getType();

    ArrayList<Task> outTasks = new ArrayList<>();
    for (int i = 0; i < taskGroup.getTasks().size(); ++i) {
      outTasks.add(taskGroup.getTask(i));
    }

    tasks.clear();
    tasks.addAll(outTasks);
    sort();
  }

  @Override
  public void setTask(Task task, int position) {
    boolean positionIsValid = position >= 0 && position < tasks.size();
    ExceptionHelper.assertTrue("Position is not valid", positionIsValid);

    tasks.set(position, task);
    sort();
  }

  @Override
  public void sort() {
    Collections.sort(tasks, Task.getComparator());
  }

  @Override
  public void addTask(Task task) {
    ExceptionHelper.checkAllObjectsNonNull("Task must be non null", task);
    boolean taskIsValid = canBelongTo(task);
    ExceptionHelper.assertTrue("Task is not valid", taskIsValid);

    int jToSet = -1;
    for (int j = 0; j < tasks.size(); ++j) {
      if (tasks.get(j).equals(task)) {
        jToSet = j;
      }
    }

    if (jToSet != -1) {
      tasks.set(jToSet, task);
    } else {
      tasks.add(task);
    }

    sort();
  }

  // --------------------------------------- getters ----------------------------------------------

  public Task.Type getType() {
    return type;
  }

  public String getDate() {
    return date;
  }

  @Override
  public ArrayList<Task> getTasks() {
    return tasks;
  }

  @Override
  public Task getTask(int position) {
    boolean positionIsValid = position >= 0 && position < tasks.size();
    ExceptionHelper.assertTrue("Position is not valid", positionIsValid);

    return tasks.get(position);
  }

  @Override
  public int getTaskSize() {
    return tasks.size();
  }

  @Override
  public Task popTask(int position) {
    boolean positionIsValid = position >= 0 && position < tasks.size();
    ExceptionHelper.assertTrue("Position is not valid", positionIsValid);

    return tasks.remove(position);
  }

}
