package com.danielkashin.taskorganiser.presentation_layer.adapter.task_groups;


import android.os.Bundle;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;

public interface ITaskGroupsAdapter {

  void attachCallbacks(Callbacks callbacks);

  void detachCallbacks();

  void addTask(Task task);

  void refreshTask(Task task);

  void saveToOutState(Bundle outState);


  interface Callbacks {

    void onTaskCreated(Task task);

    void onTaskRefreshed(Task task);

  }
}
