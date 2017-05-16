package com.danielkashin.taskorganiser.presentation_layer.presenter.notifications;


import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;
import com.danielkashin.taskorganiser.domain_layer.use_case.GetNotificationTasksUseCase;
import com.danielkashin.taskorganiser.domain_layer.use_case.SaveTaskUseCase;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.IPresenterFactory;
import com.danielkashin.taskorganiser.presentation_layer.presenter.base.Presenter;
import com.danielkashin.taskorganiser.presentation_layer.view.notifications.INotificationsView;

import java.util.ArrayList;


public class NotificationsPresenter extends Presenter<INotificationsView>
    implements INotificationsPresenter, GetNotificationTasksUseCase.Callbacks,
    SaveTaskUseCase.Callbacks {

  private final GetNotificationTasksUseCase mGetNotificationTasksUseCase;
  private final SaveTaskUseCase mSaveTaskUseCase;


  private NotificationsPresenter(GetNotificationTasksUseCase getNotificationTasksUseCase,
                                 SaveTaskUseCase saveTaskUseCase) {
    mGetNotificationTasksUseCase = getNotificationTasksUseCase;
    mSaveTaskUseCase = saveTaskUseCase;
  }


  // ------------------------------------- lifecycle ----------------------------------------------

  @Override
  protected void onViewDetached() {

  }

  @Override
  protected void onViewAttached() {

  }

  @Override
  protected void onDestroyed() {

  }

  // ----------------------------------- INotificationsPresenter ----------------------------------

  @Override
  public void onSaveTask(Task task) {
    mSaveTaskUseCase.run(this, task);
  }

  @Override
  public void onGetNotificationTasks() {
    mGetNotificationTasksUseCase.run(this);
  }

  // ----------------------------------------- callbacks ------------------------------------------

  @Override
  public void onGetNotificationTasksSuccess(ArrayList<Task> notifications) {
    if (getView() != null) {
      getView().initializeAdapter(notifications);
    }
  }

  @Override
  public void onSaveTaskSuccess(Task task) {
    mGetNotificationTasksUseCase.run(this);
  }

  @Override
  public void onGetNotificationTasksException(ExceptionBundle exceptionBundle) {
    // do nothing
  }

  @Override
  public void onSaveTaskException(ExceptionBundle exceptionBundle) {
    if (getView() != null) {
      getView().showSaveTaskError();
    }
  }


  // ---------------------------------------- inner types -----------------------------------------

  public static class Factory implements IPresenterFactory<NotificationsPresenter, INotificationsView> {

    private final GetNotificationTasksUseCase getNotificationTasksUseCase;
    private final SaveTaskUseCase saveTaskUseCase;

    public Factory(GetNotificationTasksUseCase getNotificationTasksUseCase, SaveTaskUseCase saveTaskUseCase) {
      this.getNotificationTasksUseCase = getNotificationTasksUseCase;
      this.saveTaskUseCase = saveTaskUseCase;
    }

    @Override
    public NotificationsPresenter create() {
      return new NotificationsPresenter(getNotificationTasksUseCase, saveTaskUseCase);
    }
  }

}
