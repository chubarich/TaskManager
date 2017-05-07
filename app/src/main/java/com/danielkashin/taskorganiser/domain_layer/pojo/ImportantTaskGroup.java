package com.danielkashin.taskorganiser.domain_layer.pojo;


import android.os.Parcel;
import android.os.Parcelable;

import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;

import java.util.ArrayList;
import java.util.Collections;


public class ImportantTaskGroup implements Parcelable, ITaskGroup {

  private final ArrayList<Task> tasks;


  public ImportantTaskGroup() {
    this.tasks = new ArrayList<>();
  }

  // ----------------------------------------- Parcelable -----------------------------------------

  public ImportantTaskGroup(Parcel parcel) {
    this.tasks = new ArrayList<>();
    parcel.readTypedList(this.tasks, Task.CREATOR);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int flags) {
    parcel.writeTypedList(tasks);
  }

  public static final Parcelable.Creator<DateTypeTaskGroup> CREATOR = new Parcelable.Creator<DateTypeTaskGroup>() {
    @Override
    public DateTypeTaskGroup createFromParcel(Parcel parcel) {
      return new DateTypeTaskGroup(parcel);
    }

    @Override
    public DateTypeTaskGroup[] newArray(int i) {
      return new DateTypeTaskGroup[i];
    }
  };

  // --------------------------------------- setters ----------------------------------------------

  @Override
  public void setTask(Task task, int position) {
    boolean positionIsValid = position >= 0 && position < tasks.size();
    ExceptionHelper.assertTrue("Position is not valid", positionIsValid);

    tasks.set(position, task);
  }

  @Override
  public void sort() {
    Collections.sort(tasks, Task.getComparator());
  }

  @Override
  public int addTask(Task task) {
    ExceptionHelper.checkAllObjectsNonNull("Task must be non null", task);
    boolean taskIsValid = task.getImportant();
    ExceptionHelper.assertTrue("Task is not valid", taskIsValid);

    return Task.addTaskToList(task, tasks);
  }

  // --------------------------------------- getters ----------------------------------------------

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
