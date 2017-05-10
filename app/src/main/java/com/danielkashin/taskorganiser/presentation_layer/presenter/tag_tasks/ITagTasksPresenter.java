package com.danielkashin.taskorganiser.presentation_layer.presenter.tag_tasks;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;


public interface ITagTasksPresenter {

  void onGetTaskGroupData();

  void onSaveTask(Task task);

}
