package com.danielkashin.taskorganiser.data_layer.services.remote;


import com.danielkashin.taskorganiser.data_layer.entities.remote.TaskBody;
import com.danielkashin.taskorganiser.data_layer.entities.remote.WrapperLoginBody;
import com.danielkashin.taskorganiser.data_layer.entities.remote.WrapperRegistrationBody;
import com.danielkashin.taskorganiser.data_layer.entities.remote.WrapperTasksFromServer;
import com.danielkashin.taskorganiser.data_layer.entities.remote.WrapperTasksToServer;
import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.domain_layer.pojo.Task;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ITasksRemoteService {

  Call<ResponseBody> login(String email, String password);

  Call<ResponseBody> registration(String email, String password);

  Call<WrapperTasksFromServer> sync(Long currentTimestamp, Long lastSync, ArrayList<TaskBody> tasks);

  void parseException(Exception exception) throws ExceptionBundle;

}
