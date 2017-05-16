package com.danielkashin.taskorganiser.domain_layer.pojo;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Pair;

import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskDay;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskMonth;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskNoDate;
import com.danielkashin.taskorganiser.data_layer.entities.local.data.TaskWeek;
import com.danielkashin.taskorganiser.util.DatetimeHelper;
import com.danielkashin.taskorganiser.util.ExceptionHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Task implements Parcelable {

  public static final int MAX_NAME_LENGTH = 200;
  public static final int MAX_NOTE_LENGTH = 3000;

  // ---------------------------------------- main info -------------------------------------------

  private final String UUID;

  private String name;

  private Type type;

  private String date;

  // ----------------------------------------- time -----------------------------------------------

  private Long duration;

  private Long minuteStart;

  private Long minuteEnd;

  private Long notificationTimestamp;

  // -------------------------------------- additional info ---------------------------------------

  private String note;

  private ArrayList<String> tags;

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

  public Task(TaskMonth task) {
    this.name = task.getName();
    this.UUID = task.getUUID();
    this.type = Type.Month;
    this.date = task.getDate();
    this.duration = task.getDuration();
    this.done = task.getDone() == 1;
    this.note = task.getNote();
    this.important = task.getImportant() == 1;
    this.notificationTimestamp = task.getNotificationTimestamp();
  }

  public Task(TaskWeek task) {
    this.name = task.getName();
    this.UUID = task.getUUID();
    this.type = Type.Week;
    this.date = task.getDate();
    this.duration = task.getDuration();
    this.done = task.getDone() == 1;
    this.note = task.getNote();
    this.important = task.getImportant() == 1;
    this.notificationTimestamp = task.getNotificationTimestamp();
  }

  public Task(TaskDay task) {
    this.name = task.getName();
    this.UUID = task.getUUID();
    this.type = Type.Day;
    this.date = task.getDate();
    this.duration = task.getDuration();
    this.done = task.getDone() == 1;
    this.note = task.getNote();
    this.important = task.getImportant() == 1;
    this.minuteStart = task.getMinuteStart();
    this.minuteEnd = task.getMinuteEnd();
    this.notificationTimestamp = task.getNotificationTimestamp();
  }

  public Task(TaskNoDate task) {
    this.name = task.getName();
    this.UUID = task.getUUID();
    this.type = Type.NoDate;
    this.duration = task.getDuration();
    this.done = task.getDone() == 1;
    this.note = task.getNote();
    this.important = task.getImportant() == 1;
    this.notificationTimestamp = task.getNotificationTimestamp();
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

  public boolean canSetDuration() {
    return type != Task.Type.Day || minuteStart == null || minuteEnd == null;
  }

  public boolean isValid() {
    boolean nameIsValid = name != null && name.length() < MAX_NAME_LENGTH && name.trim().length() > 0;
    boolean noteIsValid = note == null || note.length() < MAX_NOTE_LENGTH;

    return nameIsValid && noteIsValid;
  }

  public static boolean tagIsValid(String tag) {
    return tag != null && tag.length() < 26 && tag.length() > 1;
  }

  public boolean equals(Task other) {
    return this.type == other.getType() && this.UUID.equals(other.getUUID());
  }

  public String getNotificationTimestampToString(String label, String notSet) {
    label = (label == null) ? "" : (label + ": ");
    notSet = (notSet == null) ? "" : notSet;

    if (notificationTimestamp != null) {
      return label + DatetimeHelper.getDatetimeFromTimestamp(notificationTimestamp);
    } else {
      return label + notSet;
    }
  }

  public String getTimeToString(String from, String to, String durationLabel,
                                String notSet, String timeLabel, boolean forceDuration) {
    from = (from == null) ? "" : (from + " ");
    to = (to == null) ? "" : (to + " ");
    timeLabel = (timeLabel == null) ? "" : (timeLabel + ": ");
    durationLabel = (durationLabel == null) ? "" : (durationLabel + ": ");
    notSet = (notSet == null) ? "" : notSet;

    String time;
    if (type == Type.Day && minuteStart != null && minuteEnd != null) {
      if (!forceDuration) {
        time = timeLabel + String.format("%02d:%02d - %02d:%02d", minuteStart / 60, minuteStart % 60,
            minuteEnd / 60, minuteEnd % 60);
      } else {
        Long duration = minuteEnd - minuteStart;
        time = durationLabel + String.format("%01d:%02d", duration / 60, duration % 60);
      }
    } else if (duration != null && forceDuration) {
      time = durationLabel + String.format("%01d:%02d", duration / 60, duration % 60);
    } else if (type == Type.Day && minuteStart != null) {
      if (!forceDuration) {
        time = timeLabel + from + String.format("%02d:%02d", minuteStart / 60, minuteStart % 60);
      } else {
        return "";
      }
    } else if (type == Type.Day && minuteEnd != null) {
      if (!forceDuration) {
        time = timeLabel + to + String.format("%02d:%02d", minuteEnd / 60, minuteEnd % 60);
      } else {
        return "";
      }
    } else if (duration != null) {
      time = durationLabel + String.format("%01d:%02d", duration / 60, duration % 60);
    } else {
      time = durationLabel + notSet;
    }

    return time;
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
    return duration;
  }

  public void setDuration(Long duration) {
    this.duration = (duration == null || duration == 0) ? null : duration;
  }

  public Long getMinuteStart() {
    return minuteStart;
  }

  public void setType(Task.Type type) {
    this.type = type;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean setMinuteStart(Long minuteStart) {
    if (minuteStart == null) {
      this.minuteStart = null;
      return true;
    } else if (minuteEnd == null) {
      this.minuteStart = minuteStart;
      if (duration != null && (minuteStart + duration < 24 * 60)) {
        this.minuteEnd = minuteStart + duration;
      } else {
        this.duration = null;
      }
      return true;
    } else if (minuteEnd - minuteStart > 0) {
      this.minuteStart = minuteStart;
      this.duration = minuteEnd - minuteStart;
      return true;
    } else {
      this.minuteStart = minuteStart;
      this.duration = null;
      this.minuteEnd = null;
      return true;
    }
  }

  public boolean setMinuteEnd(Long minuteEnd) {
    if (minuteEnd == null) {
      this.minuteEnd = null;
      return true;
    } else if (minuteStart == null) {
      this.minuteEnd = minuteEnd;
      if (duration != null && (minuteEnd - duration >= 0)) {
        this.minuteStart = minuteEnd - duration;
      } else {
        this.duration = null;
      }
      return true;
    } else if (minuteEnd - minuteStart > 0) {
      this.minuteEnd = minuteEnd;
      this.duration = minuteEnd - minuteStart;
      return true;
    } else {
      this.minuteEnd = minuteEnd;
      this.minuteStart = null;
      this.duration = null;
      return true;
    }
  }

  public Long getMinuteEnd() {
    return minuteEnd;
  }

  public Long getNotificationTimestamp() {
    return notificationTimestamp;
  }

  public void setNotificationTimestamp(Long notificationTimestamp) {
    this.notificationTimestamp = notificationTimestamp;
  }

  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }

  public ArrayList<String> getTags() {
    return tags;
  }

  public void setTags(ArrayList<String> tags) {
    this.tags = tags;
  }

  public Boolean getDone() {
    return done != null && done;
  }


  public Task getCopy() {
    Task task = new Task(name, UUID, type, date);
    task.setDone(done);
    task.setChangedLocal(changedLocal);
    task.setDeletedLocal(deletedLocal);
    task.setChangeOrDeleteLocalTimestamp(changeOrDeleteLocalTimestamp);
    task.setImportant(important);
    task.setDuration(duration);
    task.setNote(note);
    task.setTags(tags);

    if (type == Type.Day) {
      task.setMinuteStart(minuteStart);
      task.setMinuteEnd(minuteEnd);
      task.setNotificationTimestamp(notificationTimestamp);
    }

    return task;
  }

  public static int compareInRandomGroup(Task o1, Task other) {
    if (o1.getDone() && !other.getDone()) {
      return -1;
    } else if (other.getDone() && !o1.getDone()) {
      return 1;
    }

    if (o1.getImportant() && !other.getImportant()) {
      return 1;
    } else if (other.getImportant() && !o1.getImportant()) {
      return -1;
    }

    if (o1.getDuration() != null && other.getDuration() == null) {
      return 1;
    } else if (other.getDuration() != null && o1.getDuration() == null) {
      return -1;
    }

    if (o1.getChangeOrDeleteLocalTimestamp() != null && other.getChangeOrDeleteLocalTimestamp() != null) {
      if (o1.getChangeOrDeleteLocalTimestamp() > other.getChangeOrDeleteLocalTimestamp()) {
        return 1;
      } else if (other.getChangeOrDeleteLocalTimestamp() > o1.getChangeOrDeleteLocalTimestamp()) {
        return -1;
      }
    }

    return 0;
  }

  public static int compareInDateTypeGroup(Task o1, Task other) {
    if (o1.getDone() && !other.getDone()) {
      return -1;
    } else if (other.getDone() && !o1.getDone()) {
      return 1;
    }

    boolean o1HasTime = o1.getMinuteStart() != null || o1.getMinuteEnd() != null;
    boolean otherHasTime = other.getMinuteStart() != null || other.getMinuteEnd() != null;
    if (o1HasTime && !otherHasTime) {
      return 1;
    } else if (!o1HasTime && otherHasTime) {
      return -1;
    }

    if (o1HasTime && otherHasTime) {
      if (o1.getMinuteStart() > other.getMinuteStart()) {
        return 1;
      } else {
        return -1;
      }
    }

    if (o1.getImportant() && !other.getImportant()) {
      return 1;
    } else if (other.getImportant() && !o1.getImportant()) {
      return -1;
    }

    if (o1.getDuration() != null && other.getDuration() == null) {
      return 1;
    } else if (o1.getDuration() == null && other.getDuration() != null) {
      return -1;
    }

    if (o1.getChangeOrDeleteLocalTimestamp() != null && other.getChangeOrDeleteLocalTimestamp() != null) {
      if (o1.getChangeOrDeleteLocalTimestamp() > other.getChangeOrDeleteLocalTimestamp()) {
        return 1;
      } else if (other.getChangeOrDeleteLocalTimestamp() > o1.getChangeOrDeleteLocalTimestamp()) {
        return -1;
      }
    }

    return 0;
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
    NoDate
  }

}
