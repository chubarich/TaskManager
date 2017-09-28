package com.danielkashin.taskorganiser.presentation_layer.presenter.task_groups;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.pojo.DateTypeTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetDateTypeTaskGroupsUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.view.task_groups.ITaskGroupsView;

import java.util.ArrayList;


public class TaskGroupsPresenter extends Presenter<ITaskGroupsView>
    implements ITaskGroupsPresenter, GetDateTypeTaskGroupsUseCase.Callbacks, SaveTaskUseCase.Callbacks {

  private final GetDateTypeTaskGroupsUseCase mGetTaskGroupUseCase;
  private final SaveTaskUseCase mSaveTaskUseCase;


  public TaskGroupsPresenter(GetDateTypeTaskGroupsUseCase getTaskGroupUseCase, SaveTaskUseCase saveTaskUseCase) {
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

  // ------------------------------ GetDateTypeTaskGroupsUseCase.Callbacks ---------------------------------

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
  public void onGetTaskGroupsData(Task.Type type, String date) {
    mGetTaskGroupUseCase.run(this, type, date);
  }

  @Override
  public void onSaveTask(Task task) {
    mSaveTaskUseCase.run(this, task);
  }

  // --------------------------------------- inner types ------------------------------------------

  public static class Factory implements IPresenterFactory<TaskGroupsPresenter, ITaskGroupsView> {

    private final GetDateTypeTaskGroupsUseCase getTaskGroupUseCase;
    private final SaveTaskUseCase saveTaskUseCase;

    public Factory(GetDateTypeTaskGroupsUseCase getTaskGroupUseCase, SaveTaskUseCase saveTaskUseCase) {
      this.getTaskGroupUseCase = getTaskGroupUseCase;
      this.saveTaskUseCase = saveTaskUseCase;
    }

    @Override
    public TaskGroupsPresenter create() {
      return new TaskGroupsPresenter(getTaskGroupUseCase, saveTaskUseCase);
    }
  }
}
