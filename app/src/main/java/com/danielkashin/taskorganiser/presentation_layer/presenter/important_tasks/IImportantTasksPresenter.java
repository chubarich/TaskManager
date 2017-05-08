package com.danielkashin.taskorganiser.presentation_layer.presenter.important_tasks;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;


public interface IImportantTasksPresenter {

  void onGetTaskGroupData();

  void onSaveTask(Task task);

}
