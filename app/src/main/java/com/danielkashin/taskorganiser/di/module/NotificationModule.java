package com.danielkashin.taskorganiser.di.module;

import android.content.Context;

import com.danielkashin.taskorganiser.data_layer.managers.INotificationManager;
import com.danielkashin.taskorganiser.data_layer.managers.NotificationManager;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;


@Module
public class NotificationModule {

  @Provides
  @Singleton
  public INotificationManager provideNotificationManager(Context context) {
    return new NotificationManager(context);
  }


}
