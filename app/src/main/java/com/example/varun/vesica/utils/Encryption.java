package com.example.varun.vesica.utils;

import android.util.Log;

import com.scottyab.aescrypt.AESCrypt;

import java.security.GeneralSecurityException;

/**
 * Created by varun on 20/9/16.
 */
public final class Encryption {
    private static final String TAG="Encryption";

    private Encryption(){
        //No constructor here
    }

    public static String Encrypt(String decryptedText){
        String password = "password";
        String encryptedMsg="";
        try {
            encryptedMsg = AESCrypt.encrypt(password, decryptedText);
        }catch (GeneralSecurityException e){
            //handle error
            Log.e(TAG,e.toString());
        }
        return encryptedMsg;
    }


    public static String Decrypt(String encryptedText){
        String decryptedText="";
        String password = "password";
        try {
            decryptedText = AESCrypt.decrypt(password, encryptedText);
        }catch (GeneralSecurityException e){
            //handle error - could be due to incorrect password or tampered encryptedMsg
            Log.e("decryption error ",e.toString());
        }
        return decryptedText;
    }
}
