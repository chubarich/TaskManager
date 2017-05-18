package com.danielkashin.taskorganiser.presentation_layer.presenter.authentication;


public interface IAuthenticationPresenter {

  void onRegisterOrLogin(String email, String password, boolean isLogin);

}
