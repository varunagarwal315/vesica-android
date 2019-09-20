package com.example.varun.vesica.baseclass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.varun.vesica.BuildConfig;

import java.security.Security;

/**
 * Created by varun on 19/9/16.
 */
public abstract class VesicaActivity extends AppCompatActivity {

    private String TAG;
    private BroadcastReceiver mReceiver;
    Boolean _checkNetworkState = false;
    Snackbar sbInternet;
    protected Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
                    if(checkIfNetworkIsConnected(context)){
                    /*if (isOnline()){
                        //connection and internet is there
                        Toast.makeText(context, "Internet connection is now available", Toast.LENGTH_SHORT).show();
                    }else {
                        //connection but no internet
                        Toast.makeText(context, "No internet connection is available", Toast.LENGTH_SHORT).show();
                    }*/
                        try{
                            sbInternet.dismiss();
                        }catch (Exception e){}

                    }else {
                        //no connection is available
                        displaySnackBar("No internet connection has been detected", Snackbar.LENGTH_INDEFINITE);
                    }
                    _checkNetworkState=false;
                }else if (intent.getAction().equals("android.intent.action.MEDIA_SCANNER_SCAN_FILE" )){
                    Toast.makeText(context,"Stop taking screenshots ",Toast.LENGTH_SHORT).show();
                }
            }
        };
        //End of broadcast receiver
        this.registerReceiver(mReceiver, intentFilter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try{
            if (mReceiver!=null){
                this.unregisterReceiver(mReceiver);
            }
        }catch (IllegalArgumentException  e){e.printStackTrace();}

    }
    public Boolean checkIfNetworkIsConnected(Context context){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public void displaySnackBar(String message, int num){
        sbInternet= Snackbar
                .make(findViewById(android.R.id.content), message, num);
        View snackBarView = sbInternet.getView();
        snackBarView.setBackgroundColor(Color.GRAY);
        sbInternet.show();
    }

    public void showShortToast(@NonNull String param){
        Toast.makeText(mContext, param, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(@NonNull String param){
        Toast.makeText(mContext, param, Toast.LENGTH_LONG).show();
    }

    public void myStartActivity(@NonNull Class<?> cls, Bundle bundle){
        Intent intent= new Intent(mContext,cls);
        if (bundle!=null){
            intent.putExtras(bundle);
        }
        startActivity(intent);
    }

    public void setContext(@NonNull Context context){
        this.mContext = context;
    }

    public void setTAG(){
        if (mContext==null)
            throw new NullPointerException("Context must be set first!!");
        else
        TAG= mContext.getClass().getSimpleName();
    }

    //Handles logs
    //todo: Replace with Lumber Yard and integrate with crashylytics
    public void LogD(String param){
        if (BuildConfig.DEBUG)Log.d(TAG, param);
    }

    public void LogW(String param){
        if (BuildConfig.DEBUG) Log.w(this.TAG, param);
    }

    public void LogE(String param){
        if (BuildConfig.DEBUG) Log.e(this.TAG, param);
    }

    public void LogV(String param){
        if (BuildConfig.DEBUG)Log.v(this.TAG, param);
    }


    @Override
    protected void onPause() {
        super.onPause();
        //TODO: Do something here to prevent screenshots as well
    }
}