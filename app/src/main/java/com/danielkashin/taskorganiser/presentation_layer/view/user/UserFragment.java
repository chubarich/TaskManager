package com.danielkashin.taskorganiser.presentation_layer.view.user;


import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.data_layer.managers.INotificationManager;
import com.danielkashin.taskorganiser.data_layer.managers.NotificationManager;
import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.data_layer.repository.TasksRepository;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.data_layer.services.preferences.PreferencesService;
import com.danielkashin.taskorganiser.data_layer.services.remote.ITasksRemoteService;
import com.danielkashin.taskorganiser.data_layer.services.remote.TasksRemoteService;
import com.danielkashin.taskorganiser.domain_layer.use_case.SyncUseCase;
import com.danielkashin.taskorganiser.presentation_layer.application.ITasksLocalServiceProvider;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.user.UserPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterFragment;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IMainDrawerView;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IToolbarContainer;
import com.danielkashin.taskorganiser.util.DatetimeHelper;

public class UserFragment extends PresenterFragment<UserPresenter, IUserView>
    implements IUserView {

  private TextView textEmail;
  private TextView textTimestamp;
  private Button buttonSync;
  private Button buttonLeave;


  public static UserFragment getInstance() {
    return new UserFragment();
  }

  @Override
  public void onStart() {
    super.onStart();
    
    refreshData();
    ((IToolbarContainer) getActivity()).setToolbar("Настройки", false, false, false, false);
  }

  @Override
  protected IUserView getViewInterface() {
    return this;
  }

  @Override
  protected IPresenterFactory<UserPresenter, IUserView> getPresenterFactory() {
    ITasksLocalService tasksLocalService = ((ITasksLocalServiceProvider) getActivity()
        .getApplication())
        .getTasksLocalService();
    INotificationManager notificationManager = new NotificationManager(getContext());

    ITasksRemoteService tasksRemoteService = new TasksRemoteService();

    ITasksRepository tasksRepository = TasksRepository.Factory.create(
        tasksLocalService,
        tasksRemoteService,
        notificationManager);

    SyncUseCase syncUseCase = new SyncUseCase(tasksRepository, AsyncTask.THREAD_POOL_EXECUTOR);

    return new UserPresenter.Factory(syncUseCase);
  }

  @Override
  protected int getFragmentId() {
    return this.getClass().getSimpleName().hashCode();
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_user;
  }

  @Override
  protected void initializeView(View view) {
    textEmail = (TextView) view.findViewById(R.id.text_email);
    textTimestamp = (TextView) view.findViewById(R.id.text_time);
    buttonLeave = (Button) view.findViewById(R.id.button_leave);
    buttonSync = (Button) view.findViewById(R.id.button_sync);

    buttonLeave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        ((IMainDrawerView) getActivity()).clearUser();
      }
    });

    buttonSync.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        //new AlertDialog.Builder(getContext())
        //    .setTitle("Интернет-соединение отсутствует")
        //    .create()
        //    .show();
        PreferencesService preferencesService = new PreferencesService(getContext());
        preferencesService.saveLastSync(DatetimeHelper.getCurrentTimestamp());
        refreshData();
      }
    });
  }

  private void refreshData() {
    PreferencesService preferencesService = new PreferencesService(getContext());
    if (!preferencesService.getCurrentEmail().equals("")) {
      textEmail.setText("Вы вошли как: " + preferencesService.getCurrentEmail());
      textEmail.setText("Время последней синхронизации: " + (
          preferencesService.getLastSyncTimestamp() == -1
              ? "не синхронизировано"
              : DatetimeHelper.getDatetimeFromTimestamp(preferencesService.getLastSyncTimestamp())));
    }
  }
}
