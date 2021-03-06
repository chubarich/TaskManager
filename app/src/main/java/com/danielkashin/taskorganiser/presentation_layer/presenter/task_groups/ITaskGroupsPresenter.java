package com.danielkashin.taskorganiser.presentation_layer.presenter.task_groups;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;


public interface ITaskGroupsPresenter {

  void onGetTaskGroupsData(Task.Type type, String date);

  void onSaveTask(Task task);

}
