package com.danielkashin.taskorganiser.domain_layer.use_case;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskVoid;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;

import static com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskVoid.RepositoryRunnableVoid;
import static com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskVoid.PostExecuteListenerVoid;

import java.util.concurrent.Executor;


public class SaveTagUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;
  private RepositoryAsyncTaskVoid saveTag;


  public SaveTagUseCase(ITasksRepository tasksRepository, Executor executor) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }

  public void run(final Callbacks callbacks, final String tag) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks, tag);

    RepositoryRunnableVoid saveTaskRunnable = new RepositoryRunnableVoid() {
      @Override
      public void run() throws ExceptionBundle {
        tasksRepository.saveTag(tag);
      }
    };

    PostExecuteListenerVoid saveTaskListener = new PostExecuteListenerVoid() {
      @Override
      public void onResult() {
        callbacks.onSaveTagSuccess(tag);
      }

      @Override
      public void onException(ExceptionBundle exception) {
        callbacks.onSaveTagException(exception);
      }
    };

    saveTag = new RepositoryAsyncTaskVoid(saveTaskRunnable, saveTaskListener);
    saveTag.executeOnExecutor(executor);
  }

  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onSaveTagSuccess(String tag);

    void onSaveTagException(ExceptionBundle exceptionBundle);

  }
}
