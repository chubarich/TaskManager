package com.danielkashin.taskorganiser.domain_layer.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;
import com.danielkashin.taskorganiser.presentation_layer.adapter.tags.ITagsAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class DateTypeTaskGroup implements Parcelable, ITaskGroup {

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
  public boolean canBelongTo(Task task) {
    return task.getType() == type && task.getDate().equals(date);
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
    boolean taskIsValid = task.getDate().equals(this.date) && task.getType() == this.type;
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
