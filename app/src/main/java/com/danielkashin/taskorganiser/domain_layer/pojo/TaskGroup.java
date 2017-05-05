package com.danielkashin.taskorganiser.domain_layer.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;

import java.util.ArrayList;
import java.util.Arrays;


public class TaskGroup implements Parcelable {

  private final String date;
  private final Task.Type type;
  private final ArrayList<Task> tasks;


  public TaskGroup(String date, Task.Type type) {
    this.date = date;
    this.type = type;
    this.tasks = new ArrayList<>();
  }

  // ----------------------------------------- Parcelable -----------------------------------------

  public TaskGroup(Parcel parcel) {
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

  public static final Parcelable.Creator<TaskGroup> CREATOR = new Parcelable.Creator<TaskGroup>() {
    @Override
    public TaskGroup createFromParcel(Parcel parcel) {
      return new TaskGroup(parcel);
    }

    @Override
    public TaskGroup[] newArray(int i) {
      return new TaskGroup[i];
    }
  };

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
