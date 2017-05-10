package com.danielkashin.taskorganiser.domain_layer.use_case;


import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskVoid;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.util.ExceptionHelper;

import java.util.concurrent.Executor;

public class DeleteTagUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;
  private RepositoryAsyncTaskVoid deleteTag;


  public DeleteTagUseCase(ITasksRepository tasksRepository, Executor executor) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }

  public void run(final Callbacks callbacks, final String tag) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks, tag);

    RepositoryAsyncTaskVoid.RepositoryRunnableVoid deleteTagRunnable =
        new RepositoryAsyncTaskVoid.RepositoryRunnableVoid() {
          @Override
          public void run() throws ExceptionBundle {
            tasksRepository.deleteTag(tag);
          }
        };

    RepositoryAsyncTaskVoid.PostExecuteListenerVoid deleteTagListener =
        new RepositoryAsyncTaskVoid.PostExecuteListenerVoid() {
          @Override
          public void onResult() {
            callbacks.onDeleteTagSuccess();
          }

          @Override
          public void onException(ExceptionBundle exception) {
            callbacks.onDeleteTagException(exception);
          }
        };


    deleteTag = new RepositoryAsyncTaskVoid(deleteTagRunnable, deleteTagListener);
    deleteTag.executeOnExecutor(executor);
  }

  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onDeleteTagSuccess();

    void onDeleteTagException(ExceptionBundle exceptionBundle);

  }

}
