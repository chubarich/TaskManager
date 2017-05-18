package com.danielkashin.taskorganiser.presentation_layer.presenter.authentication;

import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.view.authentication.IAuthenticationView;


public class AuthenticationPresenter extends Presenter<IAuthenticationView>
    implements IAuthenticationPresenter {


  public AuthenticationPresenter() {

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


  public static class Factory implements IPresenterFactory<AuthenticationPresenter, IAuthenticationView> {


    public Factory() {
    }

    @Override
    public AuthenticationPresenter create() {
      return new AuthenticationPresenter();
    }
  }



}
