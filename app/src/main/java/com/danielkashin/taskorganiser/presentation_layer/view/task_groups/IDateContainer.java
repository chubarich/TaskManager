package com.danielkashin.taskorganiser.presentation_layer.view.task_groups;

import android.util.Pair;

import com.danielkashin.taskorganiser.domain_layer.pojo.Task;


public interface IDateContainer {

  Pair<String, Task.Type> getParentDate();

  Pair<String, Task.Type> getUpDate();

  Pair<String, Task.Type> getDownDate();

}
