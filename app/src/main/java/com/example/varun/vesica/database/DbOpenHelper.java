package com.example.varun.vesica.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.varun.vesica.database.DbConstants.DROP_TABLE;
import static com.example.varun.vesica.database.tables.ContactsTable.CREATE_CONTACTS_TABLE;
import static com.example.varun.vesica.database.tables.ConversationsTable.CREATE_TABLE_CONVERSATIONS;
import static com.example.varun.vesica.database.tables.MessagesTable.CREATE_TABLE_CHAT_MESSAGES;
/**
 * Created by varun on 11/10/16.
 */
public class DbOpenHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME ="vesica_chat.db";
    protected static final Integer DATABASE_VERSION =1;

    public DbOpenHelper(Context context) {
        super(context, DATABASE_NAME, null /*factory*/, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CHAT_MESSAGES);
        db.execSQL(CREATE_TABLE_CONVERSATIONS);
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE+CREATE_TABLE_CHAT_MESSAGES);
        db.execSQL(DROP_TABLE+ CREATE_TABLE_CONVERSATIONS);
        db.execSQL(DROP_TABLE+ CREATE_CONTACTS_TABLE);
        onCreate(db);
    }

}
