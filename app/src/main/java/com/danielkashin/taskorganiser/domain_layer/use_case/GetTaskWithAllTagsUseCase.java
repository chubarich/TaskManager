package com.danielkashin.taskorganiser.domain_layer.use_case;


import android.util.Pair;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.util.ExceptionHelper;

import static com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse.RepositoryRunnableResponse;
import static com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse.PostExecuteListenerResponse;

import java.util.ArrayList;
import java.util.concurrent.Executor;


public class GetTaskWithAllTagsUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;

  private RepositoryAsyncTaskResponse<Pair<Task,ArrayList<String>>> getTask;


  public GetTaskWithAllTagsUseCase(ITasksRepository tasksRepository, Executor executor) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }

  public void run(final Callbacks callbacks, final Task.Type type, final String UUID) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks);

    RepositoryRunnableResponse<Pair<Task,ArrayList<String>>> getTaskRunnable =
        new RepositoryRunnableResponse<Pair<Task,ArrayList<String>>>() {
          @Override
          public Pair<Task,ArrayList<String>> run() throws ExceptionBundle {
            return tasksRepository.getTaskWithAllTags(type, UUID);
          }
        };

    PostExecuteListenerResponse<Pair<Task,ArrayList<String>>> getTaskListener =
        new PostExecuteListenerResponse<Pair<Task,ArrayList<String>>>() {
          @Override
          public void onResult(Pair<Task,ArrayList<String>> result) {
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

    void onGetTaskSuccess(Pair<Task,ArrayList<String>> task);

    void onGetTaskException(ExceptionBundle exceptionBundle);

  }
}
