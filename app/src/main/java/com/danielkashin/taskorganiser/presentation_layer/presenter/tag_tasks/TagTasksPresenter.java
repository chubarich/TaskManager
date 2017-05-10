package com.danielkashin.taskorganiser.presentation_layer.presenter.tag_tasks;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.pojo.TagTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTagTaskGroupUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.view.tag_tasks.ITagTasksView;


public class TagTasksPresenter extends Presenter<ITagTasksView>
    implements ITagTasksPresenter, GetTagTaskGroupUseCase.Callbacks, SaveTaskUseCase.Callbacks {

  private final GetTagTaskGroupUseCase mGetTagTaskGroupUseCase;
  private final SaveTaskUseCase mSaveTaskUseCase;


  public TagTasksPresenter(GetTagTaskGroupUseCase getTagTaskGroupUseCase,
                                 SaveTaskUseCase saveTaskUseCase) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter arguments must be non null",
        getTagTaskGroupUseCase, saveTaskUseCase);

    mGetTagTaskGroupUseCase = getTagTaskGroupUseCase;
    mSaveTaskUseCase = saveTaskUseCase;
  }


  // --------------------------------------- Presenter --------------------------------------------

  @Override
  protected void onViewDetached() {

  }

  @Override
  protected void onViewAttached() {

  }

  @Override
  protected void onDestroyed() {

  }

  // ------------------------------- GetTagTaskGroupUseCase.Callbacks -----------------------------

  @Override
  public void onGetTagTaskGroupSuccess(TagTaskGroup taskGroup) {
    if (getView() != null) {
      getView().initializeAdapter(taskGroup);
    }
  }

  @Override
  public void onGetTagTaskGroupException(ExceptionBundle exceptionBundle) {
    // do nothing
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

  // ------------------------------------ ITagTasksPresenter --------------------------------------

  @Override
  public void onGetTaskGroupData() {
    mGetTagTaskGroupUseCase.run(this);
  }

  @Override
  public void onSaveTask(Task task) {
    mSaveTaskUseCase.run(this, task);
  }


  // --------------------------------------- inner types ------------------------------------------

  public static class Factory implements IPresenterFactory<TagTasksPresenter, ITagTasksView> {

    private final GetTagTaskGroupUseCase getTaskGroupUseCase;
    private final SaveTaskUseCase saveTaskUseCase;

    public Factory(GetTagTaskGroupUseCase getTaskGroupUseCase, SaveTaskUseCase saveTaskUseCase) {
      this.getTaskGroupUseCase = getTaskGroupUseCase;
      this.saveTaskUseCase = saveTaskUseCase;
    }

    @Override
    public TagTasksPresenter create() {
      return new TagTasksPresenter(getTaskGroupUseCase, saveTaskUseCase);
    }
  }
}
