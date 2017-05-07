package com.danielkashin.taskorganiser.presentation_layer.view.important_tasks;

import com.danielkashin.taskorganiser.domain_layer.pojo.ImportantTaskGroup;
import com.danielkashin.taskorganiser.presentation_layer.view.base.IView;


public interface IImportantTasksView extends IView {

  void initializeAdapter(ImportantTaskGroup taskGroup);

}
