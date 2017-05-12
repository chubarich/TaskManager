package com.danielkashin.taskorganiser.domain_layer.use_case;


import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskVoid;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.util.ExceptionHelper;

import java.util.concurrent.Executor;

public class DeleteDoneTasksUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;
  private RepositoryAsyncTaskVoid deleteDoneTasks;


  public DeleteDoneTasksUseCase(ITasksRepository tasksRepository, Executor executor) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }

  public void run(final Callbacks callbacks) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks);

    RepositoryAsyncTaskVoid.RepositoryRunnableVoid deleteTagRunnable =
        new RepositoryAsyncTaskVoid.RepositoryRunnableVoid() {
          @Override
          public void run() throws ExceptionBundle {
            tasksRepository.deleteDoneData();
          }
        };

    RepositoryAsyncTaskVoid.PostExecuteListenerVoid deleteTagListener =
        new RepositoryAsyncTaskVoid.PostExecuteListenerVoid() {
          @Override
          public void onResult() {
            callbacks.onDeleteDoneTasksSuccess();
          }

          @Override
          public void onException(ExceptionBundle exception) {
            callbacks.onDeleteDoneTasksException(exception);
          }
        };


    deleteDoneTasks = new RepositoryAsyncTaskVoid(deleteTagRunnable, deleteTagListener);
    deleteDoneTasks.executeOnExecutor(executor);
  }

  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onDeleteDoneTasksSuccess();

    void onDeleteDoneTasksException(ExceptionBundle exceptionBundle);

  }
}
