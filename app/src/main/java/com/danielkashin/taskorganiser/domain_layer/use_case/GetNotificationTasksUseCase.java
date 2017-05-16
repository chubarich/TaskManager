package com.danielkashin.taskorganiser.domain_layer.use_case;


import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.util.ExceptionHelper;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class GetNotificationTasksUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;
  private RepositoryAsyncTaskResponse<ArrayList<Task>> getTasks;


  public GetNotificationTasksUseCase(ITasksRepository tasksRepository, Executor executor) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }

  public void run(final Callbacks callbacks) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks);

    RepositoryAsyncTaskResponse.RepositoryRunnableResponse<ArrayList<Task>> getTasksRunnable =
        new RepositoryAsyncTaskResponse.RepositoryRunnableResponse<ArrayList<Task>>() {
          @Override
          public ArrayList<Task> run() throws ExceptionBundle {
            return tasksRepository.getAllTasksWithNotifications();
          }
        };

    RepositoryAsyncTaskResponse.PostExecuteListenerResponse<ArrayList<Task>> getTasksListener =
        new RepositoryAsyncTaskResponse.PostExecuteListenerResponse<ArrayList<Task>>() {
          @Override
          public void onResult(ArrayList<Task> result) {
            callbacks.onGetNotificationTasksSuccess(result);
          }

          @Override
          public void onException(ExceptionBundle exception) {
            callbacks.onGetNotificationTasksException(exception);
          }
        };

    getTasks = new RepositoryAsyncTaskResponse<>(getTasksRunnable, getTasksListener);
    getTasks.executeOnExecutor(executor);
  }


  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onGetNotificationTasksSuccess(ArrayList<Task> notifications);

    void onGetNotificationTasksException(ExceptionBundle exceptionBundle);

  }
}
