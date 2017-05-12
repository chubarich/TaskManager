package com.danielkashin.taskorganiser.presentation_layer.view.main_drawer;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;


public interface ITaskViewOpener {

  void onOpenTaskView(Task.Type type, String UUID);

}
