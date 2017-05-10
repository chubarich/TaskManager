package com.danielkashin.taskorganiser.domain_layer.use_case;


import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;

import java.util.concurrent.Executor;


public class GetNoDateTaskGroupUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;

  private RepositoryAsyncTaskResponse<DateTypeTaskGroup> getTaskGroup;


  public GetNoDateTaskGroupUseCase(ITasksRepository tasksRepository, Executor executor) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }

  public void run(final Callbacks callbacks) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks);

    RepositoryAsyncTaskResponse.RepositoryRunnableResponse<DateTypeTaskGroup> getTaskGroupRunnable =
        new RepositoryAsyncTaskResponse.RepositoryRunnableResponse<DateTypeTaskGroup>() {
          @Override
          public DateTypeTaskGroup run() throws ExceptionBundle {
            return tasksRepository.getNoDateData();
          }
        };

    RepositoryAsyncTaskResponse.PostExecuteListenerResponse<DateTypeTaskGroup> getTaskGroupListener =
        new RepositoryAsyncTaskResponse.PostExecuteListenerResponse<DateTypeTaskGroup>() {
          @Override
          public void onResult(DateTypeTaskGroup result) {
            callbacks.onGetNoDateTaskGroupSuccess(result);
          }

          @Override
          public void onException(ExceptionBundle exception) {
            callbacks.onGetNoDateTaskGroupException(exception);
          }
        };

    getTaskGroup = new RepositoryAsyncTaskResponse<>(getTaskGroupRunnable, getTaskGroupListener);
    getTaskGroup.executeOnExecutor(executor);
  }

  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onGetNoDateTaskGroupSuccess(DateTypeTaskGroup taskGroup);

    void onGetNoDateTaskGroupException(ExceptionBundle exceptionBundle);

  }
}
