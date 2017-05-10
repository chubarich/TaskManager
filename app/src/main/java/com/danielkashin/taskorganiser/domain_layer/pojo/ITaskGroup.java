package com.danielkashin.taskorganiser.domain_layer.pojo;

import java.util.ArrayList;


public interface ITaskGroup {

  void setTask(Task task, int position);

  void sort();

  void addTask(Task task);

  ArrayList<Task> getTasks();

  Task getTask(int position);

  int getTaskSize();

  Task popTask(int position);

  void initialize(ITaskGroup taskGroup);

  boolean canBelongTo(Task task);

}
