package com.danielkashin.taskorganiser.presentation_layer.view.authentication;


import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.data_layer.managers.INotificationManager;
import com.danielkashin.taskorganiser.data_layer.managers.NotificationManager;
import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.data_layer.repository.TasksRepository;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.data_layer.services.preferences.PreferencesService;
import com.danielkashin.taskorganiser.presentation_layer.application.ITasksLocalServiceProvider;
import com.danielkashin.taskorganiser.presentation_layer.presenter.authentication.AuthenticationPresenter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterFragment;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IMainDrawerView;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IToolbarContainer;


public class AuthenticationFragment extends PresenterFragment<AuthenticationPresenter, IAuthenticationView>
    implements IAuthenticationView {

  private EditText editEmail;
  private EditText editPassword;
  private Button buttonRegistration;
  private Button buttonLogin;


  public static AuthenticationFragment getInstance() {
    return new AuthenticationFragment();
  }

  @Override
  public void onStart() {
    super.onStart();

    setListeners();
    ((IToolbarContainer) getActivity()).setToolbar("Регистрация", false, false, false, false);
  }

  @Override
  protected IAuthenticationView getViewInterface() {
    return this;
  }

  @Override
  protected IPresenterFactory<AuthenticationPresenter, IAuthenticationView> getPresenterFactory() {
    ITasksLocalService tasksLocalService = ((ITasksLocalServiceProvider) getActivity().getApplication())
        .getTasksLocalService();
    INotificationManager notificationManager = new NotificationManager(getContext());


    ITasksRepository tasksRepository = TasksRepository.Factory.create(
        tasksLocalService,
        notificationManager);

    return new AuthenticationPresenter.Factory();
  }

  @Override
  protected int getFragmentId() {
    return this.getClass().getSimpleName().hashCode();
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_authentication;
  }

  @Override
  protected void initializeView(View view) {
    editEmail = (EditText) view.findViewById(R.id.edit_username);
    editPassword = (EditText) view.findViewById(R.id.edit_password);
    buttonLogin = (Button) view.findViewById(R.id.button_enter);
    buttonRegistration = (Button) view.findViewById(R.id.button_registration);
  }

  private void setListeners() {
    buttonLogin.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new AlertDialog.Builder(getContext())
            .setTitle("Сервер не подключен")
            .create()
            .show();
      }
    });

    buttonRegistration.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        new AlertDialog.Builder(getContext())
            .setTitle("Сервер не подключен")
            .create()
            .show();
      }
    });

  }
}
