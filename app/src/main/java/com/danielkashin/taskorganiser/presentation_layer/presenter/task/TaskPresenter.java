package com.danielkashin.taskorganiser.presentation_layer.presenter.task;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.view.task.ITaskView;
import com.danielkashin.taskorganiser.util.ExceptionHelper;


public class TaskPresenter extends Presenter<ITaskView> implements
    ITaskPresenter, GetTaskUseCase.Callbacks {

  private final GetTaskUseCase mGetTaskUseCase;

  public TaskPresenter(GetTaskUseCase getTaskUseCase) {
    ExceptionHelper.checkAllObjectsNonNull("", getTaskUseCase);

    mGetTaskUseCase = getTaskUseCase;
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
    mGetTaskUseCase.run(this, type, UUID);
  }

  // ----------------------------------- GetTaskUseCase.Callbacks ---------------------------------

  @Override
  public void onGetTaskSuccess(Task task) {
    if (getView() != null) {
      getView().attachTask(task);
    }
  }

  @Override
  public void onGetTaskException(ExceptionBundle exceptionBundle) {

  }

  // ----------------------------------------- inner types ----------------------------------------

  public static class Factory implements IPresenterFactory<TaskPresenter, ITaskView> {

    private final GetTaskUseCase getTaskUseCase;

    public Factory(GetTaskUseCase getTaskUseCase) {
      this.getTaskUseCase = getTaskUseCase;
    }

    @Override
    public TaskPresenter create() {
      return new TaskPresenter(getTaskUseCase);
    }
  }
}
