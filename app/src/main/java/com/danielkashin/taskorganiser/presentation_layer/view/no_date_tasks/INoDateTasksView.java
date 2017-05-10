package com.danielkashin.taskorganiser.presentation_layer.view.no_date_tasks;

import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.ImportantTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.presentation_layer.view.base.IView;


public interface INoDateTasksView extends IView {

  void initializeAdapter(DateTypeTaskGroup taskGroup);

  void addTaskToViewInterface(Task task);

}
