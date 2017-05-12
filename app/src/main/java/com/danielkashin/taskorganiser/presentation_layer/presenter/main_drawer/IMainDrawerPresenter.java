package com.danielkashin.taskorganiser.presentation_layer.presenter.main_drawer;


import com.danielkashin.taskorganiser.domain_layer.pojo.Task;

public interface IMainDrawerPresenter {

  void onGetTags();

  void onSaveTag(String tag);

  void onDeleteTag(String tag);

  void onDeleteDoneTasks();

  void onDeleteTask(Task.Type type, String UUID);

  void onSaveTask(Task task);

}
