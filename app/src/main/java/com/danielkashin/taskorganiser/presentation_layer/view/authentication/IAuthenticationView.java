package com.danielkashin.taskorganiser.presentation_layer.view.authentication;


import android.util.Pair;

import com.danielkashin.taskorganiser.presentation_layer.view.base.IView;

public interface IAuthenticationView extends IView {

  void showNoInternetConnection();

  void showError();

  void openUserPage(Pair<String, String> result);

}
