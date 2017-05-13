package com.danielkashin.taskorganiser.presentation_layer.presenter.task;

import android.util.Pair;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTaskWithAllTagsUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.view.task.ITaskView;
import com.danielkashin.taskorganiser.util.ExceptionHelper;

import java.util.ArrayList;


public class TaskPresenter extends Presenter<ITaskView> implements
    ITaskPresenter, GetTaskWithAllTagsUseCase.Callbacks {

  private final GetTaskWithAllTagsUseCase mGetTaskWithAllTagsUseCase;


  public TaskPresenter(GetTaskWithAllTagsUseCase getTaskWithAllTagsUseCase) {
    ExceptionHelper.checkAllObjectsNonNull("", getTaskWithAllTagsUseCase);

    mGetTaskWithAllTagsUseCase = getTaskWithAllTagsUseCase;
  }

  // --------------------------------------- lifecycle --------------------------------------------

  @Override
  protected void onViewDetached() {

  }

  @Override
  protected void onViewAttached() {

  }

  @Override
  protected void onDestroyed() {

  }

  // --------------------------------------- ITaskPresenter ---------------------------------------

  @Override
  public void onGetTask(Task.Type type, String UUID) {
    mGetTaskWithAllTagsUseCase.run(this, type, UUID);
  }

  // ----------------------------------- GetTaskWithAllTagsUseCase.Callbacks ---------------------------------

  @Override
  public void onGetTaskSuccess(Pair<Task,ArrayList<String>> result) {
    if (getView() != null) {
      getView().attachTaskWithTags(result.first, result.second);
    }
  }

  @Override
  public void onGetTaskException(ExceptionBundle exceptionBundle) {

  }

  // ----------------------------------------- inner types ----------------------------------------

  public static class Factory implements IPresenterFactory<TaskPresenter, ITaskView> {

    private final GetTaskWithAllTagsUseCase getTaskWithAllTagsUseCase;

    public Factory(GetTaskWithAllTagsUseCase getTaskWithAllTagsUseCase) {
      this.getTaskWithAllTagsUseCase = getTaskWithAllTagsUseCase;
    }

    @Override
    public TaskPresenter create() {
      return new TaskPresenter(getTaskWithAllTagsUseCase);
    }
  }
}
