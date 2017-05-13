package com.danielkashin.taskorganiser.presentation_layer.view.task;

import android.util.Pair;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.presentation_layer.view.base.IView;

import java.util.ArrayList;


public interface ITaskView extends IView {

  void attachTaskWithTags(Task task, ArrayList<String> tags);

  Task getCurrentTask();

}
