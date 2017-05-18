package com.danielkashin.taskorganiser.data_layer.entities.remote;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WrapperTasksToServer {

  @SerializedName("date")
  @Expose
  private TasksToServer tasksToServer;

  public WrapperTasksToServer(TasksToServer tasksToServer) {
    this.tasksToServer = tasksToServer;
  }

}
