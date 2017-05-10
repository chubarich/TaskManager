package com.danielkashin.taskorganiser.domain_layer.pojo;


import com.danielkashin.taskorganiser.util.ExceptionHelper;

import java.util.ArrayList;
import java.util.Collections;


public class ImportantTaskGroup implements ITaskGroup {

  private final ArrayList<Task> tasks;


  public ImportantTaskGroup() {
    this.tasks = new ArrayList<>();
  }

  // --------------------------------------- setters ----------------------------------------------

  @Override
  public void initialize(ITaskGroup taskGroup) {
    ExceptionHelper.assertTrue("", taskGroup instanceof ImportantTaskGroup);

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


  @Override
  public boolean canBelongTo(Task task) {
    return true;
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
