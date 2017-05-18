package com.danielkashin.taskorganiser.data_layer.entities.remote;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;

public class TaskBody {


  @SerializedName("uuid")
  @Expose
  private String UUID;

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("type")
  @Expose
  private Task.Type type;

  @SerializedName("date")
  @Expose
  private String date;

  // ----------------------------------------- time -----------------------------------------------

  @SerializedName("duration")
  @Expose
  private Long duration;

  @SerializedName("minute_start")
  @Expose
  private Long minuteStart;

  @SerializedName("minute_end")
  @Expose
  private Long minuteEnd;

  @SerializedName("notification_timestamp")
  @Expose
  private Long notificationTimestamp;

  // -------------------------------------- additional info ---------------------------------------

  @SerializedName("note")
  @Expose
  private String note;

  @SerializedName("tags")
  @Expose
  private ArrayList<String> tags;

  @SerializedName("done")
  @Expose
  private Boolean done;

  @SerializedName("important")
  @Expose
  private Boolean important;

  // -------------------------------------- synchronization ---------------------------------------

  @SerializedName("deleted_local")
  @Expose
  private Boolean deletedLocal;

  @SerializedName("timestamp_of_change")
  @Expose
  private Long changeOrDeleteLocalTimestamp;


  public TaskBody(String UUID, String name, Task.Type type, String date, Long duration, Long minuteStart, Long minuteEnd, Long notificationTimestamp, String note, ArrayList<String> tags, Boolean done, Boolean important, Boolean deletedLocal, Long changeOrDeleteLocalTimestamp) {
    this.UUID = UUID;
    this.name = name;
    this.type = type;
    this.date = date;
    this.duration = duration;
    this.minuteStart = minuteStart;
    this.minuteEnd = minuteEnd;
    this.notificationTimestamp = notificationTimestamp;
    this.note = note;
    this.tags = tags;
    this.done = done;
    this.important = important;
    this.deletedLocal = deletedLocal;
    this.changeOrDeleteLocalTimestamp = changeOrDeleteLocalTimestamp;
  }
}
