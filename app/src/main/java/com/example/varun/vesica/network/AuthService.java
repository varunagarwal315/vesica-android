package com.example.varun.vesica.network;

import com.example.varun.vesica.models.AddUserModel;
import com.example.varun.vesica.models.AuthRequest;
import com.example.varun.vesica.models.AuthResponse;
import com.example.varun.vesica.models.Contact;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by varun on 22/9/16.
 */
public interface AuthService {

    @POST("/login")
    Call<AuthResponse> initAuthRequest(@Body AuthRequest AuthRequest);

    @POST("/addNewUser")
    Call<Contact> addUserReq(@Body AddUserModel userModel);

}
