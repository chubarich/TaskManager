package com.danielkashin.taskorganiser.domain_layer.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;
import com.danielkashin.taskorganiser.presentation_layer.adapter.tags.ITagsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class DateTypeTaskGroup implements Parcelable, ITaskGroup {

  private final String date;
  private final Task.Type type;
  private final ArrayList<Task> tasks;


  public DateTypeTaskGroup(String date, Task.Type type) {
    this.date = date;
    this.type = type;
    this.tasks = new ArrayList<>();
  }

  // ----------------------------------------- Parcelable -----------------------------------------

  public DateTypeTaskGroup(Parcel parcel) {
    this.date = parcel.readString();
    this.type = (Task.Type) parcel.readSerializable();
    this.tasks = new ArrayList<>();
    parcel.readTypedList(this.tasks, Task.CREATOR);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int flags) {
    parcel.writeString(date);
    parcel.writeSerializable(type);
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
    boolean taskIsValid = task.getDate().equals(this.date) && task.getType() == this.type;
    ExceptionHelper.assertTrue("Task is not valid", taskIsValid);

    return Task.addTaskToList(task, tasks);
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
