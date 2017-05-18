package com.danielkashin.taskorganiser.data_layer.entities.remote;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WrapperRegistrationBody {

  @SerializedName("date")
  @Expose
  private RegistrationBody registrationBody;

  public WrapperRegistrationBody(RegistrationBody registrationBody) {
    this.registrationBody = registrationBody;
  }


}
