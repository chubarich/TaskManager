package com.danielkashin.taskorganiser.presentation_layer.view.task_groups;

import com.danielkashin.taskorganiser.domain_layer.pojo.TaskGroup;
import com.danielkashin.taskorganiser.presentation_layer.view.base.IView;

import java.util.ArrayList;


public interface ITaskGroupsView extends IView {

  void initializeAdapter(ArrayList<TaskGroup> taskGroups);

}
