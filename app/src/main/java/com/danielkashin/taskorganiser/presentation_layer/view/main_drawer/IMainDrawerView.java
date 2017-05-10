package com.danielkashin.taskorganiser.presentation_layer.view.main_drawer;

import com.danielkashin.taskorganiser.presentation_layer.view.base.IView;

import java.util.ArrayList;


public interface IMainDrawerView extends IView {

  void showTagAlreadyExists();

  void setTags(ArrayList<String> tags);

}