package com.danielkashin.taskorganiser.presentation_layer.adapter.tasks_without_time;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;


public interface ITasksWithoutTimeAdapter {

  void setCallbacks(Callbacks callbacks);


  interface Callbacks {

    void onEditTextTextChanged(String text);

    void onTaskDoneChanged(Task task, boolean done);

  }

}
