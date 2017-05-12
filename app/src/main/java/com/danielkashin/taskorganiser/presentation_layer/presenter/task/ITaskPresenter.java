package com.danielkashin.taskorganiser.presentation_layer.presenter.task;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;


public interface ITaskPresenter {

  void onGetTask(Task.Type type, String UUID);

}
