package com.danielkashin.taskorganiser.presentation_layer.presenter.typed_tasks;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.pojo.ITaskGroup;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTypedTaskGroupUseCase;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.view.typed_tasks.ITypedTasksView;


public class TypedTasksPresenter extends Presenter<ITypedTasksView>
    implements ITypedTasksPresenter, GetTypedTaskGroupUseCase.Callbacks,
    SaveTaskUseCase.Callbacks {

  private final GetTypedTaskGroupUseCase mGetTypedTaskGroupUseCase;
  private final SaveTaskUseCase mSaveTaskUseCase;


  public TypedTasksPresenter(GetTypedTaskGroupUseCase getTypedTaskGroupUseCase,
                             SaveTaskUseCase saveTaskUseCase) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter arguments must be non null",
        getTypedTaskGroupUseCase, saveTaskUseCase);

    mGetTypedTaskGroupUseCase = getTypedTaskGroupUseCase;
    mSaveTaskUseCase = saveTaskUseCase;
  }

  // -------------------------------------- lifecycle ---------------------------------------------

  @Override
  protected void onViewDetached() {

  }

  @Override
  protected void onViewAttached() {

  }

  @Override
  protected void onDestroyed() {

  }

  // --------------------------------- SaveTaskUseCase.Callbacks ----------------------------------

  @Override
  public void onSaveTaskSuccess(Task task) {
    if (getView() != null) {
      getView().addTaskToViewInterface(task);
    }
  }

  @Override
  public void onSaveTaskException(ExceptionBundle exceptionBundle) {
    // do nothing
  }

  // ----------------------------- GetTypedTaskGroupUseCase.Callbacks ----------------------------

  @Override
  public void onGetRandomTaskGroupSuccess(ITaskGroup taskGroup) {
    if (getView() != null) {
      getView().initializeAdapter(taskGroup);
    }
  }

  @Override
  public void onGetRandomTaskGroupException(ExceptionBundle exceptionBundle) {
    // do nothing
  }

  // ----------------------------------- ITypedTasksPresenter ------------------------------------

  @Override
  public void onSaveTask(Task task) {
    mSaveTaskUseCase.run(this, task);
  }

  @Override
  public void onGetTaskGroupData() {
    mGetTypedTaskGroupUseCase.run(this);
  }

  // --------------------------------------- inner types ------------------------------------------

  public static class Factory implements IPresenterFactory<TypedTasksPresenter, ITypedTasksView> {

    private final GetTypedTaskGroupUseCase getTaskGroupUseCase;
    private final SaveTaskUseCase saveTaskUseCase;

    public Factory(GetTypedTaskGroupUseCase getTaskGroupUseCase, SaveTaskUseCase saveTaskUseCase) {
      this.getTaskGroupUseCase = getTaskGroupUseCase;
      this.saveTaskUseCase = saveTaskUseCase;
    }

    @Override
    public TypedTasksPresenter create() {
      return new TypedTasksPresenter(getTaskGroupUseCase, saveTaskUseCase);
    }
  }
}
