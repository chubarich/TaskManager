package com.danielkashin.taskorganiser.di.module;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {

  private Context applicationContext;

  public ApplicationModule(@NonNull Context context) {
    applicationContext = context.getApplicationContext();
  }

  @Provides
  @Singleton
  public Context provideContext() {
    return applicationContext;
  }

  @Provides
  @Singleton
  public Executor provideAsyncTaskExecutor() {
    return AsyncTask.THREAD_POOL_EXECUTOR;
  }


}
