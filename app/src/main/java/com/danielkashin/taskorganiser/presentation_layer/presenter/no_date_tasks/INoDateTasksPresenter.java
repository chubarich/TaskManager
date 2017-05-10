package com.danielkashin.taskorganiser.presentation_layer.presenter.no_date_tasks;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;


public interface INoDateTasksPresenter {

  void onSaveTask(Task task);

  void onGetTaskGroupData();

}
