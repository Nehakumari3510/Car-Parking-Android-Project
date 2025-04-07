package com.example.carparkingapp.register;

import com.example.carparkingapp.google_map.ParkingLocation;
import com.example.carparkingapp.login.LoginRequest;
import com.example.carparkingapp.login.LoginResponse;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {
    @POST("users")
    Call<User> postUser(@Body User user);
    
    @GET("locations") 
    Call<List<ParkingLocation>> getParkingLocations();

    @POST("login")
    Call<LoginResponse> loginUser(@Body LoginRequest loginRequest);

}
