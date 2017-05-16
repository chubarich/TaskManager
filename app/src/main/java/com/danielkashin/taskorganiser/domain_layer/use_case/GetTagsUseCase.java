package com.danielkashin.taskorganiser.domain_layer.use_case;


import android.os.AsyncTask;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;

import static com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse.PostExecuteListenerResponse;
import static com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse.RepositoryRunnableResponse;

import java.util.ArrayList;
import java.util.concurrent.Executor;

public class GetTagsUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;
  private RepositoryAsyncTaskResponse<ArrayList<String>> getTags;


  public GetTagsUseCase(ITasksRepository tasksRepository, Executor executor) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }


  public void cancel() {
    if (getTags != null && getTags.getStatus() == AsyncTask.Status.RUNNING
        &&  !getTags.isCancelled()) {
      getTags.cancel(false);
      getTags = null;
    }
  }

  public void run(final Callbacks callbacks) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks);

    RepositoryRunnableResponse<ArrayList<String>> getTagsRunnable =
        new RepositoryRunnableResponse<ArrayList<String>>() {
          @Override
          public ArrayList<String> run() throws ExceptionBundle {
            return tasksRepository.getAllTags();
          }
        };

    PostExecuteListenerResponse<ArrayList<String>> getTagsListener =
        new PostExecuteListenerResponse<ArrayList<String>>() {
          @Override
          public void onResult(ArrayList<String> result) {
            callbacks.onGetTagsSuccess(result);
          }

          @Override
          public void onException(ExceptionBundle exception) {
            callbacks.onGetTagsException(exception);
          }
        };

    getTags = new RepositoryAsyncTaskResponse<>(getTagsRunnable, getTagsListener);
    getTags.executeOnExecutor(executor);
  }


  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onGetTagsSuccess(ArrayList<String> tags);

    void onGetTagsException(ExceptionBundle exceptionBundle);

  }
}
