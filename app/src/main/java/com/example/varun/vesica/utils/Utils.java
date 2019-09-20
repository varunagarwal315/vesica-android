package com.example.varun.vesica.utils;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by varun on 25/8/16.
 */
public class Utils {

    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){}
    }



}
