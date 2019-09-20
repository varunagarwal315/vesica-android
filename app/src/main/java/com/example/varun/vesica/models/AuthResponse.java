package com.example.varun.vesica.models;

/**
 * Created by varun on 22/9/16.
 */
public class AuthResponse {
    String status;
    Integer portNumber;
    String username;

    public void setStatus(String status){
        this.status=status;
    }
    public String getStatus(){
        return this.status;
    }
    public Integer getPortNumber(){return this.portNumber;}
    public String getUsername(){return this.username;}
}
