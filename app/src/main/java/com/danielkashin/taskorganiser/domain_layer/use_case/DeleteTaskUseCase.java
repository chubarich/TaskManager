package com.danielkashin.taskorganiser.domain_layer.use_case;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskVoid;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.util.ExceptionHelper;

import java.util.concurrent.Executor;


public class DeleteTaskUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;
  private RepositoryAsyncTaskVoid deleteTag;


  public DeleteTaskUseCase(ITasksRepository tasksRepository, Executor executor) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }

  public void run(final Callbacks callbacks, final Task.Type type, final String UUID) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null",
        callbacks, type, UUID);

    RepositoryAsyncTaskVoid.RepositoryRunnableVoid deleteTaskRunnable =
        new RepositoryAsyncTaskVoid.RepositoryRunnableVoid() {
          @Override
          public void run() throws ExceptionBundle {
            tasksRepository.deleteTask(type, UUID);
          }
        };

    RepositoryAsyncTaskVoid.PostExecuteListenerVoid deleteTaskListener =
        new RepositoryAsyncTaskVoid.PostExecuteListenerVoid() {
          @Override
          public void onResult() {
            callbacks.onDeleteTaskSuccess();
          }

          @Override
          public void onException(ExceptionBundle exception) {
            callbacks.onDeleteTaskException(exception);
          }
        };


    deleteTag = new RepositoryAsyncTaskVoid(deleteTaskRunnable, deleteTaskListener);
    deleteTag.executeOnExecutor(executor);
  }

  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onDeleteTaskSuccess();

    void onDeleteTaskException(ExceptionBundle exceptionBundle);

  }
}
