package com.danielkashin.taskorganiser.domain_layer.repository;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.ImportantTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;

import java.util.ArrayList;


public interface ITasksRepository {

  // ---------------------------------------- put -------------------------------------------------

  ArrayList<DateTypeTaskGroup> getData(String date, Task.Type type) throws ExceptionBundle;

  ImportantTaskGroup getImportantData() throws ExceptionBundle;

  // ---------------------------------------- save ------------------------------------------------

  void saveTask(Task task) throws ExceptionBundle;

}
