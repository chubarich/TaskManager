package com.danielkashin.taskorganiser.domain_layer.repository;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.ImportantTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.TagTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;

import java.util.ArrayList;


public interface ITasksRepository {

  // ---------------------------------------- get -------------------------------------------------

  ArrayList<DateTypeTaskGroup> getDateTypeData(String date, Task.Type type) throws ExceptionBundle;

  DateTypeTaskGroup getNoDateData() throws ExceptionBundle;

  ImportantTaskGroup getImportantData() throws ExceptionBundle;

  TagTaskGroup getTagTaskGroup(String tag) throws ExceptionBundle;

  ArrayList<String> getTags() throws ExceptionBundle;

  // ---------------------------------------- save ------------------------------------------------

  void saveTask(Task task) throws ExceptionBundle;

  void saveTag(String tag) throws ExceptionBundle;

  // --------------------------------------- delete -----------------------------------------------

  void deleteTag(String tag);

}
