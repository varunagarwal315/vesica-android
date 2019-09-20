package com.example.varun.vesica.database;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;

/**
 * Created by varun on 11/10/16.
 */
public class DbBriteHelper {

    private static DbOpenHelper dbHelper;
    private static BriteDatabase db=null;
    private static final String TAG="DbBriteHelper";

    public static BriteDatabase getSqlBriteDb(Context context){
        if (db==null){
            Log.d(TAG,"new instance created");
            dbHelper = new DbOpenHelper(context);
            SqlBrite sqlBrite = SqlBrite.create();
            db= sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());
        }else Log.d(TAG,"OLD instance was returned");
        return db;
    }



    //Easy way of getting values from cursor results
    public static final int BOOLEAN_FALSE = 0;
    public static final int BOOLEAN_TRUE = 1;

    public static String getString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
    }

    public static boolean getBoolean(Cursor cursor, String columnName) {
        return getInt(cursor, columnName) == BOOLEAN_TRUE;
    }

    public static long getLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndexOrThrow(columnName));
    }

    public static int getInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
    }

}
