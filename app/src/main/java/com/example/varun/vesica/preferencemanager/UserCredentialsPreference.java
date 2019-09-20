package com.example.varun.vesica.preferencemanager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.varun.vesica.baseclass.VesicaApplication;

/**
 * Created by varun on 29/9/16.
 */
public class UserCredentialsPreference {
    private UserCredentialsPreference(){}


    private static SharedPreferences getSharedPreferences(){
        return VesicaApplication.getContext().getSharedPreferences("UserPreference", Context.MODE_PRIVATE);
    }


    public static String getUsername(){
        return getSharedPreferences().getString("username","");
    }


    public static void setUsername(String param){
        getSharedPreferences()
                .edit()
                .putString("username", param)
                .apply();
    }
}
