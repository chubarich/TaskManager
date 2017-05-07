package com.danielkashin.taskorganiser.presentation_layer.presenter.task_groups;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.helper.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetDateTypeTaskGroupUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.view.task_groups.ITaskGroupsView;

import java.util.ArrayList;


public class TaskGroupsPresenter extends Presenter<ITaskGroupsView>
    implements ITaskGroupsPresenter, GetDateTypeTaskGroupUseCase.Callbacks, SaveTaskUseCase.Callbacks {

  private final GetDateTypeTaskGroupUseCase mGetTaskGroupUseCase;
  private final SaveTaskUseCase mSaveTaskUseCase;


  public TaskGroupsPresenter(GetDateTypeTaskGroupUseCase getTaskGroupUseCase, SaveTaskUseCase saveTaskUseCase) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter arguments must be non null",
        getTaskGroupUseCase, saveTaskUseCase);

    mGetTaskGroupUseCase = getTaskGroupUseCase;
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

  // ------------------------------ GetDateTypeTaskGroupUseCase.Callbacks ---------------------------------

  @Override
  public void onGetDateTypeTaskGroupsSuccess(ArrayList<DateTypeTaskGroup> taskGroups) {
    if (getView() != null) {
      getView().initializeAdapter(taskGroups);
    }
  }

  @Override
  public void onGetDateTypeTaskGroupsException(ExceptionBundle exceptionBundle) {
    // do nothing
  }

  // ----------------------------------- ITaskGroupsPresenter -------------------------------------

  @Override
  public void onGetTaskGroupsData() {
    mGetTaskGroupUseCase.run(this);
  }

  @Override
  public void onSaveTask(Task task) {
    mSaveTaskUseCase.run(this, task);
  }

  // --------------------------------------- inner types ------------------------------------------

  public static class Factory implements IPresenterFactory<TaskGroupsPresenter, ITaskGroupsView> {

    private final GetDateTypeTaskGroupUseCase getTaskGroupUseCase;
    private final SaveTaskUseCase saveTaskUseCase;

    public Factory(GetDateTypeTaskGroupUseCase getTaskGroupUseCase, SaveTaskUseCase saveTaskUseCase) {
      this.getTaskGroupUseCase = getTaskGroupUseCase;
      this.saveTaskUseCase = saveTaskUseCase;
    }

    @Override
    public TaskGroupsPresenter create() {
      return new TaskGroupsPresenter(getTaskGroupUseCase, saveTaskUseCase);
    }
  }
}
