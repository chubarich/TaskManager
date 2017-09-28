package com.danielkashin.taskorganiser.di.module;

import android.content.Context;
import com.danielkashin.taskorganiser.data_layer.database.SQLiteFactory;
import com.danielkashin.taskorganiser.data_layer.managers.INotificationManager;
import com.danielkashin.taskorganiser.data_layer.repository.ITasksRepository;
import com.danielkashin.taskorganiser.data_layer.repository.TasksRepository;
import com.danielkashin.taskorganiser.data_layer.services.local.ITasksLocalService;
import com.danielkashin.taskorganiser.data_layer.services.local.TasksLocalService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class TasksModule {

  @Provides
  @Singleton
  public ITasksRepository provideTasksRepository(Context context, INotificationManager notificationManager) {
    ITasksLocalService tasksLocalService = TasksLocalService.Factory
        .create(SQLiteFactory.create(context));
    return TasksRepository.Factory.create(tasksLocalService, notificationManager);
  }

}
