package com.danielkashin.taskorganiser.domain_layer.use_case;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.pojo.TaskGroup;
import com.danielkashin.taskorganiser.domain_layer.repository.TasksRepository;

import java.util.ArrayList;
import java.util.concurrent.Executor;


public class GetWeekDataUseCase {

  private final TasksRepository tasksRepository;
  private final Executor executor;

  public GetWeekDataUseCase(TasksRepository tasksRepository, Executor executor) {
    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }

  void run(String date) {

  }

  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onGetWeekDataSuccess(ArrayList<TaskGroup> weekData);

    void onGetWeekDataException(ExceptionBundle exceptionBundle);

  }
}
