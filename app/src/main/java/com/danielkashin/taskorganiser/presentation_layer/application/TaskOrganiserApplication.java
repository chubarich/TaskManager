package com.danielkashin.taskorganiser.presentation_layer.application;

import android.app.Application;

import com.danielkashin.taskorganiser.data_layer.database.SQLiteFactory;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.data_layer.services.local.TasksLocalService;
import com.danielkashin.taskorganiser.di.component.ApplicationComponent;
import com.danielkashin.taskorganiser.di.component.DaggerApplicationComponent;
import com.danielkashin.taskorganiser.di.component.MainDrawerComponent;
import com.danielkashin.taskorganiser.di.module.ApplicationModule;
import com.danielkashin.taskorganiser.di.module.MainDrawerModule;
import com.danielkashin.taskorganiser.di.module.NotificationModule;
import com.danielkashin.taskorganiser.di.module.TasksModule;
import com.danielkashin.taskorganiser.di.module.UseCaseModule;
import com.facebook.stetho.Stetho;


public class TaskOrganiserApplication extends Application implements ITasksLocalServiceProvider {

  private static ApplicationComponent applicationComponent;

  private static volatile ITasksLocalService tasksLocalService;


  @Override
  public void onCreate() {
    super.onCreate();
    applicationComponent = DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(getApplicationContext()))
        .notificationModule(new NotificationModule())
        .tasksModule(new TasksModule())
        .useCaseModule(new UseCaseModule())
        .build();
  }

  @Override
  public ITasksLocalService getTasksLocalService() {
    // double check lock is thread safety and lazy initialization to not freeze app on start
    // getting service from activity`s interface is better than singleton for two reasons:
    // 1. single responsibility principle, that says that some class better not to
    // be responsible for representing some struct and containing exactly one instance of its struct
    // 2. data injection of more clear and obvious in case of calling some provider rather than
    // static method with public modifier available from any context

    ITasksLocalService localInstance = tasksLocalService;
    if (localInstance == null) {
      synchronized (ITasksLocalService.class) {
        localInstance = tasksLocalService;
        if (localInstance == null) {
          tasksLocalService = localInstance = TasksLocalService.Factory
              .create(SQLiteFactory.create(getApplicationContext()));
        }
      }
    }
    return localInstance;
  }

  public static ApplicationComponent getApplicationComponent() {
    return applicationComponent;
  }

  public static MainDrawerComponent getMainDrawerComponent() {
    return applicationComponent.plusMainDrawerComponent(new MainDrawerModule());
  }

}
