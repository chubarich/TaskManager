package com.danielkashin.taskorganiser.domain_layer.use_case;


import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse;
import com.danielkashin.taskorganiser.domain_layer.pojo.ITaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.presentation_layer.view.typed_tasks.TypedTasksFragment;
import com.danielkashin.taskorganiser.util.ExceptionHelper;

import java.util.concurrent.Executor;


public class GetTaskUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;

  private RepositoryAsyncTaskResponse<Task> getTask;


  public GetTaskUseCase(ITasksRepository tasksRepository, Executor executor) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }

  public void run(final Callbacks callbacks, final Task.Type type, final String UUID) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks);

    RepositoryAsyncTaskResponse.RepositoryRunnableResponse<Task> getTaskRunnable =
        new RepositoryAsyncTaskResponse.RepositoryRunnableResponse<Task>() {
          @Override
          public Task run() throws ExceptionBundle {
            return tasksRepository.getTask(type, UUID);
          }
        };

    RepositoryAsyncTaskResponse.PostExecuteListenerResponse<Task> getTaskListener =
        new RepositoryAsyncTaskResponse.PostExecuteListenerResponse<Task>() {
          @Override
          public void onResult(Task result) {
            callbacks.onGetTaskSuccess(result);
          }

          @Override
          public void onException(ExceptionBundle exception) {
            callbacks.onGetTaskException(exception);
          }
        };

    getTask = new RepositoryAsyncTaskResponse<>(getTaskRunnable, getTaskListener);
    getTask.executeOnExecutor(executor);
  }

  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onGetTaskSuccess(Task task);

    void onGetTaskException(ExceptionBundle exceptionBundle);

  }
}
