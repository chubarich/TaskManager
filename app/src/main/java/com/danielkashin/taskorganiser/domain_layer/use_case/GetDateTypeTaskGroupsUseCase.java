package com.danielkashin.taskorganiser.domain_layer.use_case;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;

import static com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse.RepositoryRunnableResponse;
import static com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse.PostExecuteListenerResponse;

import java.util.ArrayList;
import java.util.concurrent.Executor;


public class GetDateTypeTaskGroupsUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;
  private final Task.Type type;
  private final String date;

  private RepositoryAsyncTaskResponse<ArrayList<DateTypeTaskGroup>> getTaskGroup;


  public GetDateTypeTaskGroupsUseCase(ITasksRepository tasksRepository, Executor executor, Task.Type type, String date) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor, type, date);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
    this.type = type;
    this.date = date;
  }

  public void run(final Callbacks callbacks) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks);

    RepositoryRunnableResponse<ArrayList<DateTypeTaskGroup>> getTaskGroupRunnable =
        new RepositoryRunnableResponse<ArrayList<DateTypeTaskGroup>>() {
          @Override
          public ArrayList<DateTypeTaskGroup> run() throws ExceptionBundle {
            return tasksRepository.getDateTypeData(date, type);
          }
        };

    PostExecuteListenerResponse<ArrayList<DateTypeTaskGroup>> getTaskGroupListener =
        new PostExecuteListenerResponse<ArrayList<DateTypeTaskGroup>>() {
          @Override
          public void onResult(ArrayList<DateTypeTaskGroup> result) {
            callbacks.onGetDateTypeTaskGroupsSuccess(result);
          }

          @Override
          public void onException(ExceptionBundle exception) {
            callbacks.onGetDateTypeTaskGroupsException(exception);
          }
        };


    getTaskGroup = new RepositoryAsyncTaskResponse<>(getTaskGroupRunnable, getTaskGroupListener);
    getTaskGroup.executeOnExecutor(executor);
  }

  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onGetDateTypeTaskGroupsSuccess(ArrayList<DateTypeTaskGroup> taskGroups);

    void onGetDateTypeTaskGroupsException(ExceptionBundle exceptionBundle);

  }
}
