package com.danielkashin.taskorganiser.data_layer.repository;

import android.util.Pair;

import com.danielkashin.taskorganiser.data_layer.entities.remote.TaskBody;
import com.danielkashin.taskorganiser.data_layer.entities.remote.TasksFromServer;
import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.RandomTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.TagTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;

import java.util.ArrayList;


public interface ITasksRepository {

  Pair<String, String> registerOrLogin(String email, String password, boolean isLogin) throws ExceptionBundle;

  TasksFromServer sync(long lastSync, ArrayList<TaskBody> tasks) throws ExceptionBundle;


  // ---------------------------------------- get -------------------------------------------------

  ArrayList<Task> getAllTasksWithNotifications() throws ExceptionBundle;

  Pair<Task, ArrayList<String>> getTaskWithAllTags(Task.Type type, String UUID) throws ExceptionBundle;

  ArrayList<DateTypeTaskGroup> getDateTypeData(String date, Task.Type type) throws ExceptionBundle;

  DateTypeTaskGroup getNoDateData() throws ExceptionBundle;

  RandomTaskGroup getImportantData() throws ExceptionBundle;

  RandomTaskGroup getDoneData() throws ExceptionBundle;

  TagTaskGroup getTagTaskGroup(String tag) throws ExceptionBundle;

  ArrayList<String> getAllTags() throws ExceptionBundle;

  // ---------------------------------------- save ------------------------------------------------

  void saveTask(Task task) throws ExceptionBundle;

  void saveTag(String tag) throws ExceptionBundle;

  // --------------------------------------- delete -----------------------------------------------

  void deleteTask(Task.Type type, String UUID) throws ExceptionBundle;

  void deleteTag(String tag) throws ExceptionBundle;

  void deleteDoneData() throws ExceptionBundle;
}
