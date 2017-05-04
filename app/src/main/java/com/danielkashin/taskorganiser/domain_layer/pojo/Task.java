package com.danielkashin.taskorganiser.domain_layer.pojo;

import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;

import java.util.ArrayList;
import java.util.Collections;


public class Task {

  // ---------------------------------------- main info -------------------------------------------

  private final String name;

  private final String UUID;

  private final Type type;

  private final String date;

  // ----------------------------------------- time -----------------------------------------------

  private Long duration;

  private Long minuteStart;

  private Long minuteEnd;

  private String notificationDate;

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

    this.done = false;
    this.important = false;
  }

  public boolean equals(Task other) {
    return this.name.equals(other.getName()) && this.type == other.getType() && this.UUID.equals(other.getUUID());
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
        minuteEnd / 60, minuteEnd % 60 );
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

  public String getNotificationDate() {
    ExceptionHelper.assertFalse("Unavailable operation", type == Type.Mini);

    return notificationDate;
  }

  public void setNotificationDate(String notificationDate) {
    ExceptionHelper.assertFalse("Unavailable operation", type == Type.Mini);

    this.notificationDate = notificationDate;
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
    return done;
  }

  public void setDone(Boolean done) {
    this.done = done;
  }

  public Boolean getImportant() {
    return important;
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
