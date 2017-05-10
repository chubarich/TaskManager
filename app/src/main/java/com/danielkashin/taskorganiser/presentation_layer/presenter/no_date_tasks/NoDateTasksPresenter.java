package com.danielkashin.taskorganiser.presentation_layer.presenter.no_date_tasks;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetNoDateTaskGroupUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.important_tasks.IImportantTasksPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.no_date_tasks.INoDateTasksView;


public class NoDateTasksPresenter extends Presenter<INoDateTasksView>
    implements INoDateTasksPresenter, GetNoDateTaskGroupUseCase.Callbacks,
    SaveTaskUseCase.Callbacks {

  private final GetNoDateTaskGroupUseCase mGetNoDateTaskGroupUseCase;
  private final SaveTaskUseCase mSaveTaskUseCase;


  public NoDateTasksPresenter(GetNoDateTaskGroupUseCase getNoDateTaskGroupUseCase,
                              SaveTaskUseCase saveTaskUseCase) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter arguments must be non null",
        getNoDateTaskGroupUseCase, saveTaskUseCase);

    mGetNoDateTaskGroupUseCase = getNoDateTaskGroupUseCase;
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

  // ----------------------------- GetNoDateTaskGroupUseCase.Callbacks ----------------------------


  @Override
  public void onGetNoDateTaskGroupSuccess(DateTypeTaskGroup taskGroup) {
    if (getView() != null) {
      getView().initializeAdapter(taskGroup);
    }
  }

  @Override
  public void onGetNoDateTaskGroupException(ExceptionBundle exceptionBundle) {
    // do nothing
  }

  // ----------------------------------- INoDateTasksPresenter ------------------------------------

  @Override
  public void onSaveTask(Task task) {
    mSaveTaskUseCase.run(this, task);
  }

  @Override
  public void onGetTaskGroupData() {
    mGetNoDateTaskGroupUseCase.run(this);
  }

  // --------------------------------------- inner types ------------------------------------------

  public static class Factory implements IPresenterFactory<NoDateTasksPresenter, INoDateTasksView> {

    private final GetNoDateTaskGroupUseCase getTaskGroupUseCase;
    private final SaveTaskUseCase saveTaskUseCase;

    public Factory(GetNoDateTaskGroupUseCase getTaskGroupUseCase, SaveTaskUseCase saveTaskUseCase) {
      this.getTaskGroupUseCase = getTaskGroupUseCase;
      this.saveTaskUseCase = saveTaskUseCase;
    }

    @Override
    public NoDateTasksPresenter create() {
      return new NoDateTasksPresenter(getTaskGroupUseCase, saveTaskUseCase);
    }
  }
}
