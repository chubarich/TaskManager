package com.danielkashin.taskorganiser.domain_layer.repository;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.pojo.TaskGroup;

import java.util.ArrayList;


public interface ITasksRepository {

  ArrayList<TaskGroup> getWeekData(String date) throws ExceptionBundle;

  ArrayList<TaskGroup> getMonthData(String date) throws ExceptionBundle;

  ArrayList<TaskGroup> getYearData(String date) throws ExceptionBundle;

}
