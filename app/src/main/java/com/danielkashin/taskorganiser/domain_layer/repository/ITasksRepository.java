package com.danielkashin.taskorganiser.domain_layer.repository;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.pojo.TaskGroup;

import java.util.ArrayList;


public interface ITasksRepository {

  // ---------------------------------------- put -------------------------------------------------

  ArrayList<TaskGroup> getData(String date, Task.Type type) throws ExceptionBundle;

  // ---------------------------------------- save ------------------------------------------------

  void saveTask(Task task);

}
