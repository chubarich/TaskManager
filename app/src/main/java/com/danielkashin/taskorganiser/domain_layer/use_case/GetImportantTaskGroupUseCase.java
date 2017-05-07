package com.danielkashin.taskorganiser.domain_layer.use_case;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse;
import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.ImportantTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;

import java.util.ArrayList;
import java.util.concurrent.Executor;


public class GetImportantTaskGroupUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;

  private RepositoryAsyncTaskResponse<ArrayList<DateTypeTaskGroup>> getTaskGroup;


  public GetImportantTaskGroupUseCase(ITasksRepository tasksRepository, Executor executor) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }


  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onGetTaskGroupsSuccess(ArrayList<ImportantTaskGroup> taskGroups);

    void onGetTaskGroupsException(ExceptionBundle exceptionBundle);

  }
}
