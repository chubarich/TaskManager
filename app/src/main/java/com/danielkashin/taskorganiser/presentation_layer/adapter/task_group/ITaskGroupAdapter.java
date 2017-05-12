package com.danielkashin.taskorganiser.presentation_layer.adapter.task_group;

import com.danielkashin.taskorganiser.domain_layer.pojo.ITaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;


public interface ITaskGroupAdapter {

  void attachCallbacks(Callbacks mCallbacks);

  void detachCallbacks();

  void addTask(Task task);

  void changeTaskGroup(ITaskGroup taskGroup);


  interface Callbacks {

    void onTaskChanged(Task task);

    void onCreateTask(String name, String UUID, ITaskGroup taskGroup);

    void onTagClicked(String tagName);

    void onTaskClicked(Task task);
  }
}
