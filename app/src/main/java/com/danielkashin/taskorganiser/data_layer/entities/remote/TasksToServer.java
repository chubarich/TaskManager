package com.danielkashin.taskorganiser.data_layer.entities.remote;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TasksToServer {

  @SerializedName("current_timestamp")
  @Expose
  private Long currentTimestamp;

  @SerializedName("timestamp")
  @Expose
  private Long lastSynchronizationTimestamp;

  @SerializedName("changed_tasks")
  @Expose
  private ArrayList<TaskBody> changedTasks;

  public TasksToServer(Long currentTimestamp, Long lastSynchronizationTimestamp, ArrayList<TaskBody> changedTasks) {
    this.currentTimestamp = currentTimestamp;
    this.lastSynchronizationTimestamp = lastSynchronizationTimestamp;
    this.changedTasks = changedTasks;
  }

}
