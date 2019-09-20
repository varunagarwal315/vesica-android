package com.example.varun.vesica.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.example.varun.vesica.services.SocketService;

public class InitSocketReceiver extends BroadcastReceiver {
    public InitSocketReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d("InitSocketReceiver", "Starting socket service");
        Toast.makeText(context, "Starting service ", Toast.LENGTH_LONG).show();
        context.startService(new Intent(context, SocketService.class));

    }
}
