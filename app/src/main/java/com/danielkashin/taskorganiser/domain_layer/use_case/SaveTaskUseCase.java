package com.danielkashin.taskorganiser.domain_layer.use_case;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskVoid;
import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;

import static com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskVoid.RepositoryRunnableVoid;
import static com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskVoid.PostExecuteListenerVoid;

import java.util.concurrent.Executor;


public class SaveTaskUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;
  private RepositoryAsyncTaskVoid saveTask;


  public SaveTaskUseCase(ITasksRepository tasksRepository, Executor executor) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }

  public void run(final Callbacks callbacks, Task task) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks, task);

    RepositoryRunnableVoid saveTaskRunnable = new RepositoryRunnableVoid() {
          @Override
          public void run() throws ExceptionBundle {

          }
        };

    PostExecuteListenerVoid saveTaskListener = new PostExecuteListenerVoid() {
      @Override
      public void onResult() {
        callbacks.onSaveTaskSuccess();
      }

      @Override
      public void onException(ExceptionBundle exception) {
        callbacks.onSaveTaskException(exception);
      }
    };

    saveTask = new RepositoryAsyncTaskVoid(saveTaskRunnable, saveTaskListener);
    saveTask.executeOnExecutor(executor);
  }


  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onSaveTaskSuccess();

    void onSaveTaskException(ExceptionBundle exceptionBundle);

  }
}
