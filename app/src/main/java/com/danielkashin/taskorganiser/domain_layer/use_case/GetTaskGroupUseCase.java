package com.danielkashin.taskorganiser.domain_layer.use_case;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse;
import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.pojo.TaskGroup;
import com.danielkashin.taskorganiser.domain_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.domain_layer.repository.TasksRepository;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_groups.ITaskGroupsAdapter;

import static com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse.RepositoryRunnableResponse;
import static com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse.PostExecuteListenerResponse;

import java.util.ArrayList;
import java.util.concurrent.Executor;


public class GetTaskGroupUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;
  private final Task.Type type;
  private final String date;

  private RepositoryAsyncTaskResponse<ArrayList<TaskGroup>> getTaskGroup;

  public GetTaskGroupUseCase(ITasksRepository tasksRepository, Executor executor, Task.Type type, String date) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor, type, date);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
    this.type = type;
    this.date = date;
  }

  public void run(final Callbacks callbacks) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks);

    RepositoryRunnableResponse<ArrayList<TaskGroup>> getTaskGroupRunnable =
        new RepositoryRunnableResponse<ArrayList<TaskGroup>>() {
          @Override
          public ArrayList<TaskGroup> run() throws ExceptionBundle {
            return tasksRepository.getData(date, type);
          }
        };

    PostExecuteListenerResponse<ArrayList<TaskGroup>> getTaskGroupListener =
        new PostExecuteListenerResponse<ArrayList<TaskGroup>>() {
          @Override
          public void onResult(ArrayList<TaskGroup> result) {
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

    void onGetTaskGroupsSuccess(ArrayList<TaskGroup> taskGroups);

    void onGetTaskGroupsException(ExceptionBundle exceptionBundle);

  }
}
