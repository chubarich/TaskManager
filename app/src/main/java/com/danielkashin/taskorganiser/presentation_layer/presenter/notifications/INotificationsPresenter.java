package com.danielkashin.taskorganiser.presentation_layer.presenter.notifications;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;


public interface INotificationsPresenter {

  void onGetNotificationTasks();

  void onSaveTask(Task task);

}
