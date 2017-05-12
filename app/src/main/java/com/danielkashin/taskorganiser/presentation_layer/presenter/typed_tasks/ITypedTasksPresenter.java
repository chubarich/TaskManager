package com.danielkashin.taskorganiser.presentation_layer.presenter.typed_tasks;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;


public interface ITypedTasksPresenter {

  void onSaveTask(Task task);

  void onGetTaskGroupData();

}
