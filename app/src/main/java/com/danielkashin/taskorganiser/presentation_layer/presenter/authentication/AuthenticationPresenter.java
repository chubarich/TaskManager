package com.danielkashin.taskorganiser.presentation_layer.presenter.authentication;


import android.util.Pair;

import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.use_case.RegisterOrLoginUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.notifications.NotificationsPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.authentication.IAuthenticationView;
import com.danielkashin.taskorganiser.util.ExceptionHelper;

public class AuthenticationPresenter extends Presenter<IAuthenticationView>
    implements IAuthenticationPresenter, RegisterOrLoginUseCase.Callbacks {

  private final RegisterOrLoginUseCase mRegisterOrLoginUseCase;

  public AuthenticationPresenter(RegisterOrLoginUseCase registerOrLoginUseCase) {
    ExceptionHelper.checkAllObjectsNonNull("", registerOrLoginUseCase);

    mRegisterOrLoginUseCase = registerOrLoginUseCase;
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
  public void onLoginSuccess(Pair<String, String> result) {
    if (getView() != null && result != null && result.first != null && result.second != null) {
      getView().openUserPage(result);
    } else {
      getView().showError();
    }
  }

  @Override
  public void onLoginException(ExceptionBundle exceptionBundle) {
    if (getView() != null) {
      if (exceptionBundle.getReason() == ExceptionBundle.Reason.NETWORK_UNAVAILABLE) {
        getView().showNoInternetConnection();
      } else {
        getView().showError();
      }
    }
  }

  @Override
  public void onRegisterOrLogin(String email, String password, boolean isLogin) {
    mRegisterOrLoginUseCase.run(this, email, password, isLogin);
  }

  public static class Factory implements IPresenterFactory<AuthenticationPresenter, IAuthenticationView> {

    private final RegisterOrLoginUseCase registerOrLoginUseCase;

    public Factory(RegisterOrLoginUseCase registerOrLoginUseCase) {
      this.registerOrLoginUseCase = registerOrLoginUseCase;
    }

    @Override
    public AuthenticationPresenter create() {
      return new AuthenticationPresenter(registerOrLoginUseCase);
    }
  }



}
