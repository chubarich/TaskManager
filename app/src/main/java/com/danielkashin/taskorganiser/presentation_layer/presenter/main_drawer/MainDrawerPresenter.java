package com.danielkashin.taskorganiser.presentation_layer.presenter.main_drawer;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.use_case.DeleteDoneTasksUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.DeleteTagUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.DeleteTaskUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTagsUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTagUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IMainDrawerView;

import java.util.ArrayList;


public class MainDrawerPresenter extends Presenter<IMainDrawerView>
    implements IMainDrawerPresenter, GetTagsUseCase.Callbacks,
    SaveTagUseCase.Callbacks, DeleteTagUseCase.Callbacks,
    DeleteDoneTasksUseCase.Callbacks, SaveTaskUseCase.Callbacks,
    DeleteTaskUseCase.Callbacks {

  private final GetTagsUseCase mGetTagsUseCase;
  private final SaveTagUseCase mSaveTagUseCase;
  private final DeleteTagUseCase mDeleteTagUseCase;
  private final DeleteDoneTasksUseCase mDeleteDoneTasksUseCase;
  private final SaveTaskUseCase mSaveTaskUseCase;
  private final DeleteTaskUseCase mDeleteTaskUseCase;


  public MainDrawerPresenter(GetTagsUseCase getTagsUseCase,
                             SaveTagUseCase saveTagUseCase,
                             DeleteTagUseCase deleteTagUseCase,
                             DeleteDoneTasksUseCase deleteDoneTasksUseCase,
                             SaveTaskUseCase saveTaskUseCase,
                             DeleteTaskUseCase deleteTaskUseCase) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter arguments must be non null",
        getTagsUseCase, saveTagUseCase, deleteTagUseCase, deleteDoneTasksUseCase,
        saveTaskUseCase, deleteTaskUseCase);

    mGetTagsUseCase = getTagsUseCase;
    mSaveTagUseCase = saveTagUseCase;
    mDeleteTagUseCase = deleteTagUseCase;
    mDeleteDoneTasksUseCase = deleteDoneTasksUseCase;
    mSaveTaskUseCase = saveTaskUseCase;
    mDeleteTaskUseCase = deleteTaskUseCase;
  }

  // ----------------------------------------- lifecycle ------------------------------------------

  @Override
  protected void onViewDetached() {

  }

  @Override
  protected void onViewAttached() {

  }

  @Override
  protected void onDestroyed() {
    mGetTagsUseCase.cancel();
  }

  // ------------------------------------ IMainDrawerPresenter ------------------------------------

  @Override
  public void onDeleteTag(String tag) {
    mDeleteTagUseCase.run(this, tag);
  }

  @Override
  public void onGetTags() {
    mGetTagsUseCase.run(this);
  }

  @Override
  public void onSaveTag(String tag) {
    mSaveTagUseCase.run(this, tag);
  }

  @Override
  public void onDeleteDoneTasks() {
    mDeleteDoneTasksUseCase.run(this);
  }

  @Override
  public void onDeleteTask(Task.Type type, String UUID) {
    mDeleteTaskUseCase.run(this, type, UUID);
  }

  @Override
  public void onSaveTask(Task task) {
    mSaveTaskUseCase.cancel();
    mSaveTaskUseCase.run(this, task);
  }

  // ---------------------------------- DeleteTaskUseCase.Callbacks -------------------------------

  @Override
  public void onDeleteTaskSuccess() {
    if (getView() != null) {
      getView().showTaskDeletedToast();
      getView().closeCurrentFragment();
    }
  }

  @Override
  public void onDeleteTaskException(ExceptionBundle exceptionBundle) {
    if (getView() != null) {
      getView().showTaskNotDeletedToast();
    }
  }

  // ----------------------------------- SaveTaskUseCase.Callbacks --------------------------------

  @Override
  public void onSaveTaskSuccess(Task task) {
    if (getView() != null) {
      getView().showSavedToast();
    }
  }

  @Override
  public void onSaveTaskException(ExceptionBundle exceptionBundle) {
    if (getView() != null) {
      getView().showNotSavedToast();
    }
  }

  // -------------------------------- DeleteTagUseCase.Callbacks ----------------------------------

  @Override
  public void onDeleteTagSuccess() {
    mGetTagsUseCase.run(this);
  }

  @Override
  public void onDeleteTagException(ExceptionBundle exceptionBundle) {
    // do nothing
  }

  // ----------------------------- DeleteDoneTasksUseCase.Callbacks -------------------------------

  @Override
  public void onDeleteDoneTasksSuccess() {
    // do nothing
  }

  @Override
  public void onDeleteDoneTasksException(ExceptionBundle exceptionBundle) {
    // do nothing
  }

  // --------------------------------- SaveTagUseCase.Callbacks -----------------------------------

  @Override
  public void onSaveTagSuccess(String tag) {
    mGetTagsUseCase.run(this);
    if (getView() != null) {
      getView().onOpenTagView(tag);
    }
  }

  @Override
  public void onSaveTagException(ExceptionBundle exceptionBundle) {
    if (getView() != null) {
      if (exceptionBundle.getReason() == ExceptionBundle.Reason.PUT_DENIED) {
        getView().showTagAlreadyExists();
      }
    }
  }

  // --------------------------------- GetTagsUseCase.Callbacks -----------------------------------

  @Override
  public void onGetTagsSuccess(ArrayList<String> tags) {
    if (getView() != null) {
      getView().setTags(tags);
    }
  }

  @Override
  public void onGetTagsException(ExceptionBundle exceptionBundle) {
    if (getView() != null) {
      ArrayList<String> tags = new ArrayList<>();
      getView().setTags(tags);
    }
  }

  // --------------------------------------- inner types ------------------------------------------

  public static class Factory implements IPresenterFactory<MainDrawerPresenter, IMainDrawerView> {

    private final GetTagsUseCase getTagsUseCase;
    private final SaveTagUseCase saveTagUseCase;
    private final DeleteTagUseCase deleteTagUseCase;
    private final DeleteDoneTasksUseCase deleteDoneTasksUseCase;
    private final SaveTaskUseCase saveTaskUseCase;
    private final DeleteTaskUseCase deleteTaskUseCase;

    public Factory(GetTagsUseCase getTagsUseCase, SaveTagUseCase saveTagUseCase,
                   DeleteTagUseCase deleteTagUseCase, DeleteDoneTasksUseCase deleteDoneTasksUseCase,
                   SaveTaskUseCase saveTaskUseCase, DeleteTaskUseCase deleteTaskUseCase) {
      this.getTagsUseCase = getTagsUseCase;
      this.saveTagUseCase = saveTagUseCase;
      this.deleteTagUseCase = deleteTagUseCase;
      this.deleteDoneTasksUseCase = deleteDoneTasksUseCase;
      this.saveTaskUseCase = saveTaskUseCase;
      this.deleteTaskUseCase = deleteTaskUseCase;
    }

    @Override
    public MainDrawerPresenter create() {
      return new MainDrawerPresenter(getTagsUseCase, saveTagUseCase, deleteTagUseCase,
          deleteDoneTasksUseCase, saveTaskUseCase, deleteTaskUseCase);
    }
  }
}
