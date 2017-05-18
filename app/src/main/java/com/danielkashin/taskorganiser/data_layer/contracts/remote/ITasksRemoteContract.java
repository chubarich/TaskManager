package com.danielkashin.taskorganiser.data_layer.contracts.remote;


import com.danielkashin.taskorganiser.data_layer.entities.remote.LoginBody;
import com.danielkashin.taskorganiser.data_layer.entities.remote.RegistrationBody;
import com.danielkashin.taskorganiser.data_layer.entities.remote.TasksFromServer;
import com.danielkashin.taskorganiser.data_layer.entities.remote.TasksToServer;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ITasksRemoteContract {

  @POST("api/v1/sync")
  Call<TasksFromServer> sync(@Body TasksToServer wrapperTasksToServer);

}
