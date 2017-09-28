package com.danielkashin.taskorganiser.domain_layer.use_case;


import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse;
import com.danielkashin.taskorganiser.domain_layer.pojo.ITaskGroup;
import com.danielkashin.taskorganiser.presentation_layer.view.typed_tasks.TypedTasksFragment;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;

import java.util.concurrent.Executor;


public class GetTypedTaskGroupUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;

  private RepositoryAsyncTaskResponse<ITaskGroup> getTaskGroup;


  public GetTypedTaskGroupUseCase(ITasksRepository tasksRepository,
                                  Executor executor) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }

  public void run(final Callbacks callbacks, final TypedTasksFragment.State.Type type) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks);

    RepositoryAsyncTaskResponse.RepositoryRunnableResponse<ITaskGroup> getTaskGroupRunnable =
        new RepositoryAsyncTaskResponse.RepositoryRunnableResponse<ITaskGroup>() {
          @Override
          public ITaskGroup run() throws ExceptionBundle {
            if (type == TypedTasksFragment.State.Type.NoDate) {
              return tasksRepository.getNoDateData();
            } else if (type == TypedTasksFragment.State.Type.Important) {
              return tasksRepository.getImportantData();
            } else if (type == TypedTasksFragment.State.Type.Done) {
              return tasksRepository.getDoneData();
            } else {
              throw new IllegalStateException("Unhandled type");
            }
          }
        };

    RepositoryAsyncTaskResponse.PostExecuteListenerResponse<ITaskGroup> getTaskGroupListener =
        new RepositoryAsyncTaskResponse.PostExecuteListenerResponse<ITaskGroup>() {
          @Override
          public void onResult(ITaskGroup result) {
            callbacks.onGetRandomTaskGroupSuccess(result);
          }

          @Override
          public void onException(ExceptionBundle exception) {
            callbacks.onGetRandomTaskGroupException(exception);
          }
        };

    getTaskGroup = new RepositoryAsyncTaskResponse<>(getTaskGroupRunnable, getTaskGroupListener);
    getTaskGroup.executeOnExecutor(executor);
  }

  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onGetRandomTaskGroupSuccess(ITaskGroup taskGroup);

    void onGetRandomTaskGroupException(ExceptionBundle exceptionBundle);

  }
}
