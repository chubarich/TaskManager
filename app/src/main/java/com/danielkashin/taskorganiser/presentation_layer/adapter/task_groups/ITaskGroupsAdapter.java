package com.danielkashin.taskorganiser.presentation_layer.adapter.task_groups;


import android.os.Bundle;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;

public interface ITaskGroupsAdapter {

  void attachCallbacks(Callbacks callbacks);

  void detachCallbacks();

  void changeTask(Task task);


  interface Callbacks {

    void onTaskChanged(Task task);

    void onTaskLabelClicked(String date, Task.Type type);

    void onTagClicked(String tagName);

    void onTaskClicked(Task task);

  }
}
