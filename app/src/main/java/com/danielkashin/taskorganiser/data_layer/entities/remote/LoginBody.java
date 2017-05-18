package com.danielkashin.taskorganiser.data_layer.entities.remote;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginBody {


  @SerializedName("email")
  @Expose
  private String email;

  @SerializedName("password")
  @Expose
  private String password;


  public LoginBody(String email, String password) {
    this.email = email;
    this.password = password;
  }


  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }


}
