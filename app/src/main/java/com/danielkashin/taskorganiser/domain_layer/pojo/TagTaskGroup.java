package com.danielkashin.taskorganiser.domain_layer.pojo;

import com.danielkashin.taskorganiser.util.ExceptionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class TagTaskGroup implements ITaskGroup {

  private final ArrayList<Task> tasks;
  private String tag;

  public TagTaskGroup(String tag) {
    this.tasks = new ArrayList<>();
    this.tag = tag;
  }


  // --------------------------------------- setters ----------------------------------------------

  @Override
  public void initialize(ITaskGroup taskGroup) {
    ExceptionHelper.assertTrue("", taskGroup instanceof TagTaskGroup);

    tag = ((TagTaskGroup) taskGroup).getTag();

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
    Collections.sort(tasks, getComparator());
  }

  @Override
  public void addTask(Task task) {
    ExceptionHelper.checkAllObjectsNonNull("TaskBody must be non null", task);

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

  public String getTag() {
    return tag;
  }

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

  private Comparator<Task> getComparator() {
    return new Comparator<Task>() {
      @Override
      public int compare(Task o1, Task o2) {
        return Task.compareInRandomGroup(o1, o2);
      }
    };
  }
}
