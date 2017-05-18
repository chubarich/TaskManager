package com.danielkashin.taskorganiser.domain_layer.use_case;

import com.danielkashin.taskorganiser.data_layer.entities.remote.TasksFromServer;
import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.data_layer.services.preferences.PreferencesService;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.util.ExceptionHelper;

import java.util.concurrent.Executor;


public class SyncUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;
  private RepositoryAsyncTaskResponse<TasksFromServer> sync;


  public SyncUseCase(ITasksRepository tasksRepository, Executor executor) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }

  public void run(final Callbacks callbacks, final Task task, final PreferencesService preferencesService) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks, task);

    RepositoryAsyncTaskResponse.RepositoryRunnableResponse<TasksFromServer> syncRunnable =
      new RepositoryAsyncTaskResponse.RepositoryRunnableResponse<TasksFromServer>() {
        @Override
        public TasksFromServer run() throws ExceptionBundle {
          return tasksRepository.sync(preferencesService.getLastSyncTimestamp(), null);
        }
      };

    RepositoryAsyncTaskResponse.PostExecuteListenerResponse<TasksFromServer> listenerResponse =
      new RepositoryAsyncTaskResponse.PostExecuteListenerResponse<TasksFromServer>() {
        @Override
        public void onResult(TasksFromServer result) {
          callbacks.onSyncSuccess(result);
        }

        @Override
        public void onException(ExceptionBundle exception) {
          callbacks.onSyncException(exception);
        }
      };
  }


  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onSyncSuccess(TasksFromServer tasks);

    void onSyncException(ExceptionBundle exceptionBundle);

  }
}
