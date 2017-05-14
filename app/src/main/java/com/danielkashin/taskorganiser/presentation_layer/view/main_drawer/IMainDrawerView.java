package com.danielkashin.taskorganiser.presentation_layer.view.main_drawer;

import com.danielkashin.taskorganiser.presentation_layer.view.base.IView;

import java.util.ArrayList;


public interface IMainDrawerView extends ITagViewOpener, IView {

  void showTagAlreadyExists();

  void setTags(ArrayList<String> tags);

  void showSavedToast();

  void showNotSavedToast();

  void showTaskNotDeletedToast();

  void showTaskDeletedToast();

  void closeCurrentFragment();

}