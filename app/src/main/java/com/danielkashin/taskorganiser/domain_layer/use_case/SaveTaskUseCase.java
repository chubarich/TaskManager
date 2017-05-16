package com.danielkashin.taskorganiser.domain_layer.use_case;

import android.os.AsyncTask;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskVoid;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;

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

  public void cancel() {
    if (saveTask != null && saveTask.getStatus() == AsyncTask.Status.RUNNING && !saveTask.isCancelled()) {
      saveTask.cancel(false);
    }
  }

  public void run(final Callbacks callbacks, final Task task) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks, task);

    RepositoryRunnableVoid saveTaskRunnable = new RepositoryRunnableVoid() {
          @Override
          public void run() throws ExceptionBundle {
            tasksRepository.saveTask(task);
          }
        };

    PostExecuteListenerVoid saveTaskListener = new PostExecuteListenerVoid() {
      @Override
      public void onResult() {
        callbacks.onSaveTaskSuccess(task);
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

    void onSaveTaskSuccess(Task task);

    void onSaveTaskException(ExceptionBundle exceptionBundle);

  }
}
