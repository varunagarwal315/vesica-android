package com.example.varun.vesica.utils;

import android.content.Context;

import com.facebook.stetho.Stetho;

/**
 * Created by varun on 22/10/16.
 */
public class OptionalDependencies {


    private final Context context;


    public OptionalDependencies(Context context) {
        this.context = context;
    }


    public void initialize() {
        Stetho.initializeWithDefaults(context);
    }
}
