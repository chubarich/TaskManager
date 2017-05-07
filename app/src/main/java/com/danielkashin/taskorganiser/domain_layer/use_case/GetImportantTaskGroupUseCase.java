package com.danielkashin.taskorganiser.domain_layer.use_case;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse;
import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.ImportantTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.presentation_layer.adapter.tags.ITagsAdapter;

import java.util.ArrayList;
import java.util.concurrent.Executor;


public class GetImportantTaskGroupUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;

  private RepositoryAsyncTaskResponse<ImportantTaskGroup> getTaskGroup;


  public GetImportantTaskGroupUseCase(ITasksRepository tasksRepository, Executor executor) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }

  public void run(final Callbacks callbacks) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks);

    RepositoryAsyncTaskResponse.RepositoryRunnableResponse<ImportantTaskGroup> getTaskGroupRunnable =
        new RepositoryAsyncTaskResponse.RepositoryRunnableResponse<ImportantTaskGroup>() {
          @Override
          public ImportantTaskGroup run() throws ExceptionBundle {
            return tasksRepository.getImportantData();
          }
        };

    RepositoryAsyncTaskResponse.PostExecuteListenerResponse<ImportantTaskGroup > getTaskGroupListener =
        new RepositoryAsyncTaskResponse.PostExecuteListenerResponse<ImportantTaskGroup>() {
          @Override
          public void onResult(ImportantTaskGroup result) {
            callbacks.onGetTaskGroupsSuccess(result);
          }

          @Override
          public void onException(ExceptionBundle exception) {
            callbacks.onGetTaskGroupsException(exception);
          }
        };

    getTaskGroup = new RepositoryAsyncTaskResponse<>(getTaskGroupRunnable, getTaskGroupListener);
    getTaskGroup.executeOnExecutor(executor);
  }

  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onGetTaskGroupsSuccess(ImportantTaskGroup taskGroups);

    void onGetTaskGroupsException(ExceptionBundle exceptionBundle);

  }
}
