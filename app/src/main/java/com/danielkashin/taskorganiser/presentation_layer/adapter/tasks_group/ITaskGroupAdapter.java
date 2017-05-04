package com.danielkashin.taskorganiser.presentation_layer.adapter.tasks_group;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.pojo.TaskGroup;


public interface ITaskGroupAdapter {

  void attachCallbacks(Callbacks mCallbacks);

  void detachCallbacks();

  void addTask(Task task);

  void setOrRefreshTaskGroup(TaskGroup taskGroup);


  interface Callbacks {

    void onTaskCreated(Task task);

    void onTaskRefreshed(Task task);

  }

}
