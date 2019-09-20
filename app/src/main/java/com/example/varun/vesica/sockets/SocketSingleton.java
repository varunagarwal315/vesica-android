package com.example.varun.vesica.sockets;

/**
 * Created by varun on 20/8/16.
 */

import android.util.Log;

import com.example.varun.vesica.Constants;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class SocketSingleton {

    private static Socket mSocket = null;
    private static final String TAG="SocketSingleton";

    //Generate an instance
    //IO MANAGER TO BE CHANGED !!
    public static Socket getSocket(String portNumber){
        Log.d(TAG,Constants.baseIpAddress + portNumber );
        if (mSocket ==null){
            try {
                mSocket = IO.socket(Constants.baseIpAddress + portNumber);

            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
         return mSocket;
    }

    public static void setSocketNull(){
        mSocket=null;
    }
}

