package com.danielkashin.taskorganiser.domain_layer.async_task;

import android.os.AsyncTask;

import java.util.concurrent.Executor;


abstract class VoidAsyncTask<T> extends AsyncTask<Void, Void, T> {

  public void execute() {
    super.execute();
  }

  public void executeOnExecutor(Executor executor){
    super.executeOnExecutor(executor);
  }

}
