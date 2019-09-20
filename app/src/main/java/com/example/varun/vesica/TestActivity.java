package com.example.varun.vesica;

import android.os.Bundle;

import com.example.varun.vesica.baseclass.VesicaActivity;
import com.example.varun.vesica.encryption.TweetNaCl;
import com.example.varun.vesica.encryption.exceptions.KeyPairException;
import com.example.varun.vesica.sockets.SocketSingleton;

import java.io.UnsupportedEncodingException;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.varun.vesica.encryption.TweetNaCl.*;

public class TestActivity extends VesicaActivity {

    private Socket mSocket;
    @OnClick(R.id.btn)
    void login(){
        loginStuff();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        setContext(TestActivity.this);
        setTAG();


        try {
            KeyPair keyPair = TweetNaCl.cryptoBoxKeyPair();
            byte[] val =keyPair.publicKey;
            LogD(bytesToHex(val));
        } catch (KeyPairException e) {
            e.printStackTrace();
        }


        try{
            mSocket.disconnect();
        }catch (Exception e){e.printStackTrace();}

        mSocket= SocketSingleton.getSocket("3000");

        mSocket.on("identifyYourself", identifyYourself);
        mSocket.on("authStepOne", authStepOne);
        mSocket.connect();

    }

    private void loginStuff(){

    }

    private Emitter.Listener identifyYourself = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogD("identify yourself received");
            mSocket.emit("authStepOne", "");
            //mSocket.emit("login", "");
        }
    };

    private Emitter.Listener authStepOne = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            LogD("auth Step One");
            // Do stuff
            mSocket.emit("authStepTwo", "");
        }
    };

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
}
