package com.danielkashin.taskorganiser.data_layer.contracts.remote;


import com.danielkashin.taskorganiser.data_layer.entities.remote.WrapperLoginBody;
import com.danielkashin.taskorganiser.data_layer.entities.remote.WrapperRegistrationBody;
import com.danielkashin.taskorganiser.data_layer.entities.remote.WrapperTasksFromServer;
import com.danielkashin.taskorganiser.data_layer.entities.remote.WrapperTasksToServer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ITasksRemoteContract {

  @POST("api/v1/login")
  Call<ResponseBody> login(@Body WrapperLoginBody wrapperLoginBody);

  @POST("api/v1/registration")
  Call<ResponseBody> registration(@Body WrapperRegistrationBody wrapperRegistrationBody);

  @POST("api/v1/sync")
  Call<WrapperTasksFromServer> sync(@Body WrapperTasksToServer wrapperTasksToServer);


}
