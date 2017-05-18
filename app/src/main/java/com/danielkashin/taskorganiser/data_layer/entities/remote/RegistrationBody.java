package com.danielkashin.taskorganiser.data_layer.entities.remote;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RegistrationBody {

  @SerializedName("name")
  @Expose
  private String name;

  @SerializedName("email")
  @Expose
  private String email;

  @SerializedName("password")
  @Expose
  private String password;


  public RegistrationBody(String email, String password) {
    this.name = email;
    this.email = email;
    this.password = password;
  }


  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

}
