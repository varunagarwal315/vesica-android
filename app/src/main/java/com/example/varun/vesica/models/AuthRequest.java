package com.example.varun.vesica.models;

/**
 * Created by varun on 22/9/16.
 */
public class AuthRequest {

    private String userName;
    private String password;

    public void setAuthModel(String userName, String password){
        this.userName = userName;
        this.password = password;
    }

    public String getUsername(){
        return this.userName;
    }
}
