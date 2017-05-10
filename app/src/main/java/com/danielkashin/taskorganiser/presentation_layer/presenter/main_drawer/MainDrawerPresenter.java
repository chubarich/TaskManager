package com.danielkashin.taskorganiser.presentation_layer.presenter.main_drawer;


import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.use_case.DeleteTagUseCase;
import com.danielkashin.taskorganiser.util.ExceptionHelper;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTagsUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTagUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IMainDrawerView;

import java.util.ArrayList;

public class MainDrawerPresenter extends Presenter<IMainDrawerView>
    implements IMainDrawerPresenter, GetTagsUseCase.Callbacks,
    SaveTagUseCase.Callbacks, DeleteTagUseCase.Callbacks {

  private final GetTagsUseCase mGetTagsUseCase;
  private final SaveTagUseCase mSaveTagUseCase;
  private final DeleteTagUseCase mDeleteTagUseCase;


  public MainDrawerPresenter(GetTagsUseCase getTagsUseCase, SaveTagUseCase saveTagUseCase,
                             DeleteTagUseCase deleteTagUseCase) {
    ExceptionHelper.checkAllObjectsNonNull("All presenter arguments must be non null",
        getTagsUseCase, saveTagUseCase, deleteTagUseCase);

    mGetTagsUseCase = getTagsUseCase;
    mSaveTagUseCase = saveTagUseCase;
    mDeleteTagUseCase = deleteTagUseCase;
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
  public void onStart() {
    mGetTagsUseCase.run(this);
  }

  @Override
  public void onSaveTag(String tag) {
    mSaveTagUseCase.run(this, tag);
  }

  // -------------------------------- DeleteTagUseCase.Callbacks ----------------------------------

  @Override
  public void onDeleteTagSuccess() {
    // do nothing
  }

  @Override
  public void onDeleteTagException(ExceptionBundle exceptionBundle) {
    // do nothing
  }


  // --------------------------------- SaveTagUseCase.Callbacks -----------------------------------

  @Override
  public void onSaveTagSuccess() {
    mGetTagsUseCase.run(this);
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

    public Factory(GetTagsUseCase getTagsUseCase, SaveTagUseCase saveTagUseCase, DeleteTagUseCase deleteTagUseCase) {
      this.getTagsUseCase = getTagsUseCase;
      this.saveTagUseCase = saveTagUseCase;
      this.deleteTagUseCase = deleteTagUseCase;
    }

    @Override
    public MainDrawerPresenter create() {
      return new MainDrawerPresenter(getTagsUseCase, saveTagUseCase, deleteTagUseCase);
    }
  }
}
