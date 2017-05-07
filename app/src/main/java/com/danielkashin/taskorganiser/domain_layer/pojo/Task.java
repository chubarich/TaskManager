package com.danielkashin.taskorganiser.domain_layer.pojo;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskDay;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMonth;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskWeek;
import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Task implements Parcelable {

  // ---------------------------------------- main info -------------------------------------------

  private final String name;

  private final String UUID;

  private final Type type;

  private final String date;

  // ----------------------------------------- time -----------------------------------------------

  private Long duration;

  private Long minuteStart;

  private Long minuteEnd;

  private Long notificationTimestamp;

  // -------------------------------------- additional info ---------------------------------------

  private String note;

  private ArrayList<String> tags;

  private ArrayList<Long> subtasks;

  private Boolean done;

  private Boolean important;

  // -------------------------------------- synchronization ---------------------------------------

  private Boolean changedLocal;

  private Boolean deletedLocal;

  private Long changeOrDeleteLocalTimestamp;

  // ----------------------------------------------------------------------------------------------

  public Task(String name, String UUID, Type type, String date) {
    this.name = name;
    this.UUID = UUID;
    this.type = type;
    this.date = date;
  }

  public Task(TaskMonth taskMonth) {
    this.name = taskMonth.getName();
    this.UUID = taskMonth.getUUID();
    this.type = Type.Month;
    this.date = taskMonth.getDate();
    this.duration = taskMonth.getDuration();
    this.done = taskMonth.getDone() == 1;
    this.note = taskMonth.getNote();
    this.important = taskMonth.getImportant() == 1;
  }

  public Task(TaskWeek taskWeek) {
    this.name = taskWeek.getName();
    this.UUID = taskWeek.getUUID();
    this.type = Type.Week;
    this.date = taskWeek.getDate();
    this.duration = taskWeek.getDuration();
    this.done = taskWeek.getDone() == 1;
    this.note = taskWeek.getNote();
    this.important = taskWeek.getImportant() == 1;
  }

  public Task(TaskDay taskDay) {
    this.name = taskDay.getName();
    this.UUID = taskDay.getUUID();
    this.type = Type.Day;
    this.date = taskDay.getDate();
    this.duration = taskDay.getDuration();
    this.done = taskDay.getDone() == 1;
    this.note = taskDay.getNote();
    this.important = taskDay.getImportant() == 1;
    this.minuteStart = taskDay.getMinuteStart();
    this.minuteEnd = taskDay.getMinuteEnd();
    this.notificationTimestamp = taskDay.getNotificationTimestamp();
  }

  // ----------------------------------------- Parcelable -----------------------------------------

  public Task(Parcel parcel) {
    name = parcel.readString();
    UUID = parcel.readString();
    type = (Type) parcel.readSerializable();
    date = parcel.readString();
    duration = (Long) parcel.readSerializable();
    minuteStart = (Long) parcel.readSerializable();
    minuteEnd = (Long) parcel.readSerializable();
    notificationTimestamp = (Long) parcel.readSerializable();
    note = parcel.readString();
    tags = new ArrayList<>();
    parcel.readStringList(tags);
    subtasks = (ArrayList<Long>) parcel.readSerializable();
    done = (Boolean) parcel.readSerializable();
    important = (Boolean) parcel.readSerializable();
    changedLocal = (Boolean) parcel.readSerializable();
    deletedLocal = (Boolean) parcel.readSerializable();
    changeOrDeleteLocalTimestamp = (Long) parcel.readSerializable();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel parcel, int flags) {
    parcel.writeString(name);
    parcel.writeString(UUID);
    parcel.writeSerializable(type);
    parcel.writeString(date);
    parcel.writeSerializable(duration);
    parcel.writeSerializable(minuteStart);
    parcel.writeSerializable(minuteEnd);
    parcel.writeSerializable(notificationTimestamp);
    parcel.writeString(note);
    parcel.writeStringList(tags);
    parcel.writeSerializable(subtasks);
    parcel.writeSerializable(done);
    parcel.writeSerializable(important);
    parcel.writeSerializable(changedLocal);
    parcel.writeSerializable(deletedLocal);
    parcel.writeSerializable(changeOrDeleteLocalTimestamp);
  }

  public static final Parcelable.Creator<Task> CREATOR = new Parcelable.Creator<Task>() {
    @Override
    public Task createFromParcel(Parcel parcel) {
      return new Task(parcel);
    }

    @Override
    public Task[] newArray(int i) {
      return new Task[i];
    }
  };

  // ----------------------------------- getters/setters ------------------------------------------

  public boolean equals(Task other) {
    return this.type == other.getType() && this.UUID.equals(other.getUUID());
  }

  public boolean equalsWithAdditionalInformation(Task other) {
    if (!this.equals(other)) {
      return false;
    }

    if (this.tags == null && other.tags != null || this.tags != null && other.tags == null) {
      return false;
    }

    if (this.tags != null) {
      if (this.tags.size() != other.tags.size()) {
        return false;
      }

      Collections.sort(this.tags);
      Collections.sort(other.tags);

      for (int i = 0; i < this.tags.size(); ++i) {
        if (!this.tags.get(i).equals(other.tags.get(i))) {
          return false;
        }
      }
    }

    if (this.minuteStart == null && other.minuteStart != null
        || this.minuteStart != null && other.minuteStart == null
        || this.minuteEnd == null && other.minuteEnd != null
        || this.minuteEnd != null && other.minuteEnd == null) {
      return false;
    }

    if (type == Type.Day && this.minuteStart != null && this.minuteEnd != null
        && (!this.minuteStart.equals(other.minuteStart) || !this.minuteEnd.equals(other.minuteEnd))) {
      return false;
    }

    return true;
  }

  public String getTimeToString() {
    ExceptionHelper.assertTrue("Unavailable operation", type == Type.Day);

    if (this.minuteStart == null || this.minuteEnd == null) {
      return null;
    }

    return String.format("%02d:%02d - %02d:%02d", minuteStart / 60, minuteStart % 60,
        minuteEnd / 60, minuteEnd % 60);
  }

  public String getUUID() {
    return UUID;
  }

  public Type getType() {
    return type;
  }

  public String getDate() {
    return date;
  }

  public String getName() {
    return name;
  }

  public Long getDuration() {
    ExceptionHelper.assertFalse("Unavailable operation", type == Type.Mini);

    return duration;
  }

  public void setDuration(Long duration) {
    ExceptionHelper.assertFalse("Unavailable operation", type == Type.Mini);

    this.duration = duration;
  }

  public Long getMinuteStart() {
    ExceptionHelper.assertTrue("Unavailable operation", type == Type.Day);

    return minuteStart;
  }

  public void setMinuteStart(Long minuteStart) {
    ExceptionHelper.assertTrue("Unavailable operation", type == Type.Day);

    this.minuteStart = minuteStart;
  }

  public Long getMinuteEnd() {
    ExceptionHelper.assertTrue("Unavailable operation", type == Type.Day);

    return minuteEnd;
  }

  public void setMinuteEnd(Long minuteEnd) {
    ExceptionHelper.assertTrue("Unavailable operation", type == Type.Day);

    this.minuteEnd = minuteEnd;
  }

  public Long getNotificationTimestamp() {
    ExceptionHelper.assertFalse("Unavailable operation", type == Type.Mini);

    return notificationTimestamp;
  }

  public void setNotificationTimestamp(Long notificationTimestamp) {
    ExceptionHelper.assertFalse("Unavailable operation", type == Type.Mini);

    this.notificationTimestamp = notificationTimestamp;
  }

  public String getNote() {
    ExceptionHelper.assertFalse("Unavailable operation", type == Type.Mini);

    return note;
  }

  public void setNote(String note) {
    ExceptionHelper.assertFalse("Unavailable operation", type == Type.Mini);

    this.note = note;
  }

  public ArrayList<String> getTags() {
    ExceptionHelper.assertFalse("Unavailable operation", type == Type.Mini);

    return tags;
  }

  public void setTags(ArrayList<String> tags) {
    ExceptionHelper.assertFalse("Unavailable operation", type == Type.Mini);

    this.tags = tags;
  }

  public ArrayList<Long> getSubtasks() {
    ExceptionHelper.assertFalse("Unavailable operation", type == Type.Mini);

    return subtasks;
  }

  public void setSubtasks(ArrayList<Long> subtasks) {
    ExceptionHelper.assertFalse("Unavailable operation", type == Type.Mini);

    this.subtasks = subtasks;
  }

  public Boolean getDone() {
    return done != null && done;
  }


  public static int compare(Task o1, Task other) {
    if (o1.getType() != other.getType()) {
      if ((o1.getType() == Task.Type.Day && (other.getType() == Task.Type.Week || other.getType() == Task.Type.Month))
          || (o1.getType() == Task.Type.Week && other.getType() == Task.Type.Month)) {
        return -1;
      } else {
        return 1;
      }
    }

    if (o1.getType() == Task.Type.Day) {
      if (o1.getMinuteStart() != null && other.getMinuteStart() == null) {
        return 1;
      } else if (other.getMinuteStart() != null && o1.getMinuteStart() == null) {
        return -1;
      }
    }

    if (o1.getDone() && !other.getDone()) {
      return -1;
    } else if (other.getDone() && !o1.getDone()) {
      return 1;
    } else {
      return 0;
    }
  }

  public static Comparator<Task> getComparator() {
    return new Comparator<Task>() {
      @Override
      public int compare(Task o1, Task o2) {
        return Task.compare(o1, o2);
      }
    };
  }

  public static int addTaskToList(Task task, ArrayList<Task> tasks) {
    ExceptionHelper.checkAllObjectsNonNull("Task must be non null", task, tasks);

    int position = tasks.size() - 1;
    while (position > 0 && getComparator().compare(task, tasks.get(position)) < 0) {
      position--;
    }
    tasks.add(position + 1, task);

    return position;
  }

  public void setDone(Boolean done) {
    this.done = done;
  }

  public Boolean getImportant() {
    return important != null && important;
  }

  public void setImportant(Boolean important) {
    this.important = important;
  }

  public Boolean getChangedLocal() {
    return changedLocal;
  }

  public void setChangedLocal(Boolean changedLocal) {
    this.changedLocal = changedLocal;
  }

  public Boolean getDeletedLocal() {
    return deletedLocal;
  }

  public void setDeletedLocal(Boolean deletedLocal) {
    this.deletedLocal = deletedLocal;
  }

  public Long getChangeOrDeleteLocalTimestamp() {
    return changeOrDeleteLocalTimestamp;
  }

  public void setChangeOrDeleteLocalTimestamp(Long changeOrDeleteLocalTimestamp) {
    this.changeOrDeleteLocalTimestamp = changeOrDeleteLocalTimestamp;
  }


  // ---------------------------------------- inner types -----------------------------------------

  public enum Type {
    Month,
    Week,
    Day,
    Mini
  }

}
