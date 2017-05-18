package com.danielkashin.taskorganiser.domain_layer.use_case;


import android.util.Pair;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskResponse;
import com.danielkashin.taskorganiser.domain_layer.async_task.RepositoryAsyncTaskVoid;
import com.danielkashin.taskorganiser.util.ExceptionHelper;

import java.util.concurrent.Executor;


public class RegisterOrLoginUseCase {

  private final ITasksRepository tasksRepository;
  private final Executor executor;
  private RepositoryAsyncTaskResponse<Pair<String,String>> registerOrLogin;


  public RegisterOrLoginUseCase(ITasksRepository tasksRepository, Executor executor) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter objects must be non null",
        tasksRepository, executor);

    this.tasksRepository = tasksRepository;
    this.executor = executor;
  }

  public void run(final Callbacks callbacks, final String email, final String password, final boolean isLogin) {
    ExceptionHelper.checkAllObjectsNonNull("All use case run() arguments must be non null", callbacks, email, password);

    RepositoryAsyncTaskResponse.RepositoryRunnableResponse<Pair<String, String>> saveTaskRunnable =
      new RepositoryAsyncTaskResponse.RepositoryRunnableResponse<Pair<String, String>>() {
        @Override
        public Pair<String, String> run() throws ExceptionBundle {
          return tasksRepository.registerOrLogin(email, password, isLogin);
        }
      };

    RepositoryAsyncTaskResponse.PostExecuteListenerResponse<Pair<String, String>> saveTaskListener =
        new RepositoryAsyncTaskResponse.PostExecuteListenerResponse<Pair<String, String>>() {
          @Override
          public void onResult(Pair<String, String> result) {
            if (result == null) {
              callbacks.onLoginException(new ExceptionBundle(ExceptionBundle.Reason.NULL_POINTER));
            } else {
              callbacks.onLoginSuccess(result);
            }
          }

          @Override
          public void onException(ExceptionBundle exception) {
            callbacks.onLoginException(exception);
          }
        };

    registerOrLogin = new RepositoryAsyncTaskResponse<>(saveTaskRunnable, saveTaskListener);
    registerOrLogin.executeOnExecutor(executor);
  }

  // --------------------------------------- inner types ------------------------------------------

  public interface Callbacks {

    void onLoginSuccess(Pair<String, String> result);

    void onLoginException(ExceptionBundle exceptionBundle);

  }
}
