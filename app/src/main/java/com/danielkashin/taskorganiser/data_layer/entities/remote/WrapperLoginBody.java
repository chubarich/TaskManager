package com.danielkashin.taskorganiser.data_layer.entities.remote;


import com.beloo.widget.chipslayoutmanager.util.log.Log;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WrapperLoginBody {

  @SerializedName("date")
  @Expose
  private LoginBody loginBody;

  public WrapperLoginBody(LoginBody loginBody) {
    this.loginBody = loginBody;
  }

}
