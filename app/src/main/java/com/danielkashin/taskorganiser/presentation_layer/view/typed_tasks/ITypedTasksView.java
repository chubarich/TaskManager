package com.danielkashin.taskorganiser.presentation_layer.view.typed_tasks;

import com.danielkashin.taskorganiser.domain_layer.pojo.ITaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.presentation_layer.view.base.IView;


public interface ITypedTasksView extends IView {

  void initializeAdapter(ITaskGroup taskGroup);

  void addTaskToViewInterface(Task task);

  TypedTasksFragment.State.Type getType();

}
