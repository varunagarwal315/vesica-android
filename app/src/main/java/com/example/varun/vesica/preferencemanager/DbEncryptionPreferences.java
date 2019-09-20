package com.example.varun.vesica.preferencemanager;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.varun.vesica.baseclass.VesicaApplication;

/**
 * Created by varun on 17/10/16.
 */
public class DbEncryptionPreferences {
    private DbEncryptionPreferences(){}

    private static SharedPreferences getSharedPreferences(){
        return VesicaApplication.getContext().getSharedPreferences("EncryptionPreference", Context.MODE_PRIVATE);
    }

    public static void setDbEncryptionKey(String param){
        getSharedPreferences()
                .edit()
                .putString("dbEncryptionKey", param)
                .apply();
    }

    public static void setDbDecryptionKey(String param){
        getSharedPreferences()
                .edit()
                .putString("dbDecryptionKey", param)
                .apply();
    }
}
