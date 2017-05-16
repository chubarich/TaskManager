package com.danielkashin.taskorganiser.presentation_layer.view.notifications;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration;
import com.danielkashin.taskorganiser.R;
import com.danielkashin.taskorganiser.data_layer.managers.INotificationManager;
import com.danielkashin.taskorganiser.data_layer.managers.NotificationManager;
import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.data_layer.repository.TasksRepository;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.domain_layer.pojo.ITaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.TagTaskGroup;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetNotificationTasksUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetTagTaskGroupUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.adapter.notifications.INotificationsAdapter;
import com.danielkashin.taskorganiser.presentation_layer.adapter.notifications.NotificationsAdapter;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_group.ITaskGroupAdapter;
import com.danielkashin.taskorganiser.presentation_layer.adapter.task_group.TaskGroupAdapter;
import com.danielkashin.taskorganiser.presentation_layer.application.ITasksLocalServiceProvider;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.notifications.INotificationsPresenter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.notifications.NotificationsPresenter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.tag_tasks.ITagTasksPresenter;
import com.danielkashin.taskorganiser.presentation_layer.presenter.tag_tasks.TagTasksPresenter;
import com.danielkashin.taskorganiser.presentation_layer.view.base.PresenterFragment;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.ITagViewOpener;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.ITaskViewOpener;
import com.danielkashin.taskorganiser.presentation_layer.view.main_drawer.IToolbarContainer;
import com.danielkashin.taskorganiser.util.ExceptionHelper;

import java.util.ArrayList;

public class NotificationsFragment extends PresenterFragment<NotificationsPresenter, INotificationsView>
    implements INotificationsView, INotificationsAdapter.Callbacks {

  private RecyclerView mRecyclerView;


  public static NotificationsFragment getInstance() {
    return new NotificationsFragment();
  }

  // ----------------------------------- PresenterFragment ----------------------------------------

  @Override
  protected INotificationsView getViewInterface() {
    return this;
  }

  @Override
  protected IPresenterFactory<NotificationsPresenter, INotificationsView> getPresenterFactory() {
    ITasksLocalService tasksLocalService = ((ITasksLocalServiceProvider) getActivity()
        .getApplication())
        .getTasksLocalService();
    INotificationManager notificationManager = new NotificationManager(getContext());

    ITasksRepository tasksRepository = TasksRepository.Factory.create(
        tasksLocalService,
        notificationManager);


    GetNotificationTasksUseCase getTasksUseCase = new GetNotificationTasksUseCase(
        tasksRepository,
        AsyncTask.THREAD_POOL_EXECUTOR);

    SaveTaskUseCase saveTaskUseCase = new SaveTaskUseCase(
        tasksRepository,
        AsyncTask.THREAD_POOL_EXECUTOR);

    return new NotificationsPresenter.Factory(getTasksUseCase, saveTaskUseCase);
  }

  @Override
  protected int getFragmentId() {
    return this.getClass().getSimpleName().hashCode();
  }

  @Override
  protected int getLayoutRes() {
    return R.layout.fragment_recycler_container;
  }

  @Override
  protected void initializeView(View view) {
    LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
    layoutManager.setReverseLayout(true);
    layoutManager.setStackFromEnd(true);

    mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    mRecyclerView.setLayoutManager(layoutManager);
    mRecyclerView.addItemDecoration(new SpacingItemDecoration(0, 10));
    mRecyclerView.setAdapter(new NotificationsAdapter());
  }


  // ----------------------------------------- lifecycle ------------------------------------------

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public void onStart() {
    super.onStart();

    ((IToolbarContainer) getActivity()).setToolbar(getString(R.string.drawer_notifications), false, false, false, false);

    ((INotificationsPresenter) getPresenter()).onGetNotificationTasks();
  }

  @Override
  public void onStop() {
    if (mRecyclerView.getAdapter() != null) {
      ((INotificationsAdapter) mRecyclerView.getAdapter()).detachCallbacks();
    }

    super.onStop();
  }

  // ------------------------------------- INotificationView --------------------------------------

  @Override
  public void initializeAdapter(ArrayList<Task> tasks) {
    if (mRecyclerView != null && mRecyclerView.getAdapter() != null) {
      ((INotificationsAdapter)mRecyclerView.getAdapter()).attachCallbacks(this);
      ((INotificationsAdapter)mRecyclerView.getAdapter()).setTaskNotifications(tasks);
    }
  }

  @Override
  public void showSaveTaskError() {
    Toast.makeText(getContext(), getString(R.string.notification_not_deleted), Toast.LENGTH_SHORT).show();
  }

  // ------------------------------- INotificationsAdapter.Callbacks ------------------------------

  @Override
  public void onDeleteNotificationButtonClicked(Task task) {
    task.setNotificationTimestamp(null);
    ((INotificationsPresenter)getPresenter()).onSaveTask(task);
  }

  @Override
  public void onNotificationClicked(Task task) {
    ((ITaskViewOpener) getActivity()).onOpenTaskView(task.getType(), task.getUUID());
  }

}
