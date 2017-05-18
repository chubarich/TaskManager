package com.danielkashin.taskorganiser.data_layer.entities.remote;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WrapperTasksFromServer {

  @SerializedName("date")
  @Expose
  private TasksFromServer tasksFromServer;

  public TasksFromServer getTasksFromServer() {
    return tasksFromServer;
  }

}
