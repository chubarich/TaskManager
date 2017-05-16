package com.danielkashin.taskorganiser.presentation_layer.view.notifications;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.presentation_layer.view.base.IView;

import java.util.ArrayList;


public interface INotificationsView extends IView {

  void initializeAdapter(ArrayList<Task> tasks);

  void showSaveTaskError();

}
