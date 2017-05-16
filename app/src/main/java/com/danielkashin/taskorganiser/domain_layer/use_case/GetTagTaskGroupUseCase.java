package com.danielkashin.taskorganiser.domain_layer.use_case;


import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.TagTaskGroup;
import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;

import java.util.concurrent.Executor;


public class GetTagTaskGroupUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;
  private final String tag;

  private RepositoryAsyncTaskResponse<TagTaskGroup> getTaskGroup;


  public GetTagTaskGroupUseCase(ITasksRepository tasksRepository, Executor executor, String tag) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor, tag);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
    this.tag = tag;
  }

  public void run(final Callbacks callbacks) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks);

    RepositoryAsyncTaskResponse.RepositoryRunnableResponse<TagTaskGroup> getTaskGroupRunnable =
        new RepositoryAsyncTaskResponse.RepositoryRunnableResponse<TagTaskGroup>() {
          @Override
          public TagTaskGroup run() throws ExceptionBundle {
            return tasksRepository.getTagTaskGroup(tag);
          }
        };

    RepositoryAsyncTaskResponse.PostExecuteListenerResponse<TagTaskGroup> getTaskGroupListener =
        new RepositoryAsyncTaskResponse.PostExecuteListenerResponse<TagTaskGroup>() {
          @Override
          public void onResult(TagTaskGroup result) {
            callbacks.onGetTagTaskGroupSuccess(result);
          }

          @Override
          public void onException(ExceptionBundle exception) {
            callbacks.onGetTagTaskGroupException(exception);
          }
        };

    getTaskGroup = new RepositoryAsyncTaskResponse<>(getTaskGroupRunnable, getTaskGroupListener);
    getTaskGroup.executeOnExecutor(executor);
  }

  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onGetTagTaskGroupSuccess(TagTaskGroup taskGroup);

    void onGetTagTaskGroupException(ExceptionBundle exceptionBundle);

  }


}
