package com.danielkashin.taskorganiser.presentation_layer.presenter.important_tasks;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.ImportantTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetImportantTaskGroupUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.view.important_tasks.IImportantTasksView;


public class ImportantTasksPresenter extends Presenter<IImportantTasksView>
    implements IImportantTasksPresenter, GetImportantTaskGroupUseCase.Callbacks {

  private final GetImportantTaskGroupUseCase mGetImportantTaskGroupUseCase;
  private final SaveTaskUseCase mSaveTaskUseCase;


  public ImportantTasksPresenter(GetImportantTaskGroupUseCase getImportantTaskGroupUseCase,
                                 SaveTaskUseCase saveTaskUseCase) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter arguments must be non null",
        getImportantTaskGroupUseCase, saveTaskUseCase);

    mGetImportantTaskGroupUseCase = getImportantTaskGroupUseCase;
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

  // --------------------------- GetImportantTaskGroupUseCase.Callbacks ---------------------------

  @Override
  public void onGetTaskGroupsSuccess(ImportantTaskGroup taskGroups) {
    if (getView() != null) {
      getView().initializeAdapter(taskGroups);
    }
  }

  @Override
  public void onGetTaskGroupsException(ExceptionBundle exceptionBundle) {

  }

  // --------------------------------- IImportantTasksPresenter -----------------------------------

  @Override
  public void onGetTaskGroupData() {
    mGetImportantTaskGroupUseCase.run(this);
  }

  // --------------------------------------- inner types ------------------------------------------

  public static class Factory implements IPresenterFactory<ImportantTasksPresenter, IImportantTasksView> {

    private final GetImportantTaskGroupUseCase getTaskGroupUseCase;
    private final SaveTaskUseCase saveTaskUseCase;

    public Factory(GetImportantTaskGroupUseCase getTaskGroupUseCase, SaveTaskUseCase saveTaskUseCase) {
      this.getTaskGroupUseCase = getTaskGroupUseCase;
      this.saveTaskUseCase = saveTaskUseCase;
    }

    @Override
    public ImportantTasksPresenter create() {
      return new ImportantTasksPresenter(getTaskGroupUseCase, saveTaskUseCase);
    }
  }
}
