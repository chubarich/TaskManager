package com.danielkashin.taskorganiser.data_layer.entities.remote;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TasksFromServer {

  @SerializedName("current_timestamp")
  @Expose
  private Long currentTimestamp;

  @SerializedName("changed_tasks")
  @Expose
  private ArrayList<TaskBody> changedTasks;

  public Long getCurrentTimestamp() {
    return currentTimestamp;
  }

  public ArrayList<TaskBody> getChangedTasks() {
    return changedTasks;
  }

}
