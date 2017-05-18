package com.danielkashin.taskorganiser.data_layer.services.base;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public abstract class NetworkService<S> {

  private Retrofit retrofit;
  private S service;


  public NetworkService(String baseUrl){
    this.retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build())
            .build();

    bindService();
  }


  protected S getService() {
    return this.service;
  }


  private void bindService() {
    this.service = createService(this.retrofit);
  }


  protected abstract S createService(Retrofit retrofit);

}
