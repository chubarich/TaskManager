package com.danielkashin.taskorganiser.presentation_layer.presenter.main_drawer;


public interface IMainDrawerPresenter {

  void onStart();

  void onSaveTag(String tag);

  void onDeleteTag(String tag);

}
