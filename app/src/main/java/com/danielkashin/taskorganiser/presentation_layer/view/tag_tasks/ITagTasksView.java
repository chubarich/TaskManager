package com.danielkashin.taskorganiser.presentation_layer.view.tag_tasks;

import com.danielkashin.taskorganiser.domain_layer.pojo.ImportantTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.TagTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.presentation_layer.view.base.IView;


public interface ITagTasksView extends IView {

  void addTaskToViewInterface(Task task);

  void initializeAdapter(TagTaskGroup taskGroup);

  String getTagName();

}
