package com.danielkashin.taskorganiser.data_layer.services.remote;


import com.danielkashin.taskorganiser.data_layer.contracts.remote.ITasksRemoteContract;
import com.danielkashin.taskorganiser.data_layer.entities.remote.LoginBody;
import com.danielkashin.taskorganiser.data_layer.entities.remote.RegistrationBody;
import com.danielkashin.taskorganiser.data_layer.entities.remote.TaskBody;
import com.danielkashin.taskorganiser.data_layer.entities.remote.TasksToServer;
import com.danielkashin.taskorganiser.data_layer.entities.remote.WrapperLoginBody;
import com.danielkashin.taskorganiser.data_layer.entities.remote.WrapperRegistrationBody;
import com.danielkashin.taskorganiser.data_layer.entities.remote.WrapperTasksFromServer;
import com.danielkashin.taskorganiser.data_layer.entities.remote.WrapperTasksToServer;
import com.danielkashin.taskorganiser.data_layer.exceptions.ExceptionBundle;
import com.danielkashin.taskorganiser.data_layer.services.base.NetworkService;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.net.ssl.SSLException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;

public class TasksRemoteService extends NetworkService<ITasksRemoteContract>
implements ITasksRemoteService {

  public TasksRemoteService() {
    super("http://calendo.istomin.im");
  }

  @Override
  protected ITasksRemoteContract createService(Retrofit retrofit) {
    return retrofit.create(ITasksRemoteContract.class);
  }


  @Override
  public Call<ResponseBody> login(String email, String password) {
    return getService().login(new WrapperLoginBody(new LoginBody(email, password)));
  }

  @Override
  public Call<ResponseBody> registration(String email, String password) {
    return getService().registration(new WrapperRegistrationBody(new RegistrationBody(email, password)));
  }

  @Override
  public Call<WrapperTasksFromServer> sync(Long currentTimestamp, Long lastSyncTimestamp, ArrayList<TaskBody> tasks) {
    return getService().sync(new WrapperTasksToServer(new TasksToServer(currentTimestamp, lastSyncTimestamp, tasks)));
  }

  @Override
  public void parseException(Exception exception) throws ExceptionBundle {
    if (exception instanceof ExceptionBundle) {
      throw (ExceptionBundle) exception;
    } else if (exception instanceof ConnectException || exception instanceof SocketTimeoutException
        || exception instanceof UnknownHostException || exception instanceof SSLException) {
      throw new ExceptionBundle(ExceptionBundle.Reason.NETWORK_UNAVAILABLE);
    } else {
      throw new ExceptionBundle(ExceptionBundle.Reason.UNKNOWN);
    }
  }


}
