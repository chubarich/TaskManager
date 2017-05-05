package com.danielkashin.taskorganiser.presentation_layer.presenter.task_groups;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.TaskGroup;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTaskGroupUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.view.task_groups.ITaskGroupsView;

import java.util.ArrayList;


public class TaskGroupsPresenter extends Presenter<ITaskGroupsView>
    implements ITaskGroupsPresenter, GetTaskGroupUseCase.Callbacks {

  private final GetTaskGroupUseCase mGetTaskGroupUseCase;


  public TaskGroupsPresenter(GetTaskGroupUseCase getTaskGroupUseCase) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter arguments must be non null",
        getTaskGroupUseCase);

    mGetTaskGroupUseCase = getTaskGroupUseCase;
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

  // ------------------------------ GetTaskGroupUseCase.Callbacks ---------------------------------

  @Override
  public void onGetTaskGroupsSuccess(ArrayList<TaskGroup> taskGroups) {
    if (getView() != null) {
      getView().initializeAdapter(taskGroups);
    }
  }

  @Override
  public void onGetTaskGroupsException(ExceptionBundle exceptionBundle) {
    // do nothing
  }

  // ----------------------------------- ITaskGroupsPresenter -------------------------------------

  @Override
  public void onGetTaskGroupsData() {
    mGetTaskGroupUseCase.run(this);
  }

  // --------------------------------------- inner types ------------------------------------------

  public static class Factory implements IPresenterFactory<TaskGroupsPresenter, ITaskGroupsView> {

    private final GetTaskGroupUseCase getTaskGroupUseCase;

    public Factory(GetTaskGroupUseCase getTaskGroupUseCase) {
      this.getTaskGroupUseCase = getTaskGroupUseCase;
    }

    @Override
    public TaskGroupsPresenter create() {
      return new TaskGroupsPresenter(getTaskGroupUseCase);
    }
  }
}
