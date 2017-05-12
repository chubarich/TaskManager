package com.danielkashin.taskorganiser.presentation_layer.view.task;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.presentation_layer.view.base.IView;


public interface ITaskView extends IView {

  void attachTask(Task task);

  Task getCurrentTask();

}
