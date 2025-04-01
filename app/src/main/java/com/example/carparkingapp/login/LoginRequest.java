package com.example.carparkingapp.login;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("userEmail")
    private final String userEmail;
    @SerializedName("userPassword")
    private final String userPassword;

    public LoginRequest(String userEmail, String userPassword) {
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }
    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }
}
