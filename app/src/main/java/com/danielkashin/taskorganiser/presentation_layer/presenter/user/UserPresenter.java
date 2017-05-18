package com.danielkashin.taskorganiser.presentation_layer.presenter.user;

import com.danielkashin.taskorganiser.data_layer.entities.remote.TasksFromServer;
import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.use_case.RegisterOrLoginUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SyncUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.authentication.AuthenticationPresenter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.view.authentication.IAuthenticationView;
import com.danielkashin.taskorganiser.presentation_layer.view.user.IUserView;


public class UserPresenter extends Presenter<IUserView>
implements IUserPresenter, SyncUseCase.Callbacks{

  private SyncUseCase syncUseCase;

  public UserPresenter(SyncUseCase syncUseCase) {
    this.syncUseCase = syncUseCase;
  }

  @Override
  protected void onViewDetached() {

  }

  @Override
  protected void onViewAttached() {

  }

  @Override
  protected void onDestroyed() {

  }

  @Override
  public void onSyncSuccess(TasksFromServer tasks) {

  }

  @Override
  public void onSyncException(ExceptionBundle exceptionBundle) {

  }


  public static class Factory implements IPresenterFactory<UserPresenter, IUserView> {

    private final SyncUseCase syncUseCase;

    public Factory(SyncUseCase syncUseCase) {
      this.syncUseCase = syncUseCase;
    }

    @Override
    public UserPresenter create() {
      return new UserPresenter(syncUseCase);
    }
  }
}
