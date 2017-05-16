package com.danielkashin.taskorganiser.presentation_layer.adapter.notifications;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;

import java.util.ArrayList;


public interface INotificationsAdapter {

  void attachCallbacks(Callbacks callbacks);

  void detachCallbacks();

  void setTaskNotifications(ArrayList<Task> notifications);


  interface Callbacks {

    void onDeleteNotificationButtonClicked(Task task);

    void onNotificationClicked(Task task);

  }
}
