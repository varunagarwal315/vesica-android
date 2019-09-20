package com.example.varun.vesica.baseclass;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.varun.vesica.utils.OptionalDependencies;

/**
 * Created by varun on 19/9/16.
 */
public class VesicaApplication extends Application {

    private Integer portNumber;
    private static Context context;
    private static final String TAG="VesicaApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        //TODO: REMOVE THIS LATER
        new OptionalDependencies(this).initialize();

//        stopService(new Intent(this, SocketService.class));
//
//        if (!StringUtils.isBlank(UserCredentialsPreference.getUsername())){
//            if (!isMyServiceRunning(SocketService.class)){
//                Log.d(TAG,"Starting service");
//                startService(new Intent(this, SocketService.class));
//                Toast.makeText(context,"Started service", Toast.LENGTH_SHORT).show();
//            }else Log.d(TAG,"Service is already running");
//        }else Log.d(TAG, "Username is empty");
    }

    public static Context getContext(){
        return context;
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.d(TAG, service.service.getClassName());
                return true;
            }
        }
        return false;
    }
}
