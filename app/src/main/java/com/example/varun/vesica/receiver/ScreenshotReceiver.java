package com.example.varun.vesica.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

//NOT WORKING. :(
public class ScreenshotReceiver extends BroadcastReceiver {
    public ScreenshotReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w("OnRecieve ","was called");
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if (intent.getAction().equals("android.net.conn.MEDIA_SCANNER_SCAN_FILE")){
            Log.d("SCREENSHOT", " WAS TAKEN");
        }else if(intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")){
            Log.d("NETWORK CHANGED"," ");
            Toast.makeText(context,"NO INTERNET LA !!",Toast.LENGTH_LONG).show();
        }
    }
}
