package com.example.varun.vesica.database.tables;


import android.content.ContentValues;

import static com.example.varun.vesica.database.DbConstants.COLUMN_ID;
import static com.example.varun.vesica.database.DbConstants.COMMA;
import static com.example.varun.vesica.database.DbConstants.CREATE_TABLE;
import static com.example.varun.vesica.database.DbConstants.INTEGER;
import static com.example.varun.vesica.database.DbConstants.PRIMARY_KEY;
import static com.example.varun.vesica.database.DbConstants.TEXT;

/**
 * Created by varun on 10/10/16.
 */
public class ContactsTable {

    public static final String CONTACTS_TABLE_NAME ="contact_table_name";
    public static final String CONTACTS_TABLE_KEY_USERNAME="contact_table_key_username";
    public static final String CONTACTS_TABLE_KEY_DISPLAY_NAME="contact_table_key_display_name";
    public static final String CONTACTs_TABLE_KEY_IS_VALIDATED="contact_table_key_is_validated";


    public static final String CREATE_CONTACTS_TABLE = CREATE_TABLE + CONTACTS_TABLE_NAME+
            "("
            + COLUMN_ID + PRIMARY_KEY + COMMA
            + CONTACTS_TABLE_KEY_USERNAME + TEXT + COMMA
            + CONTACTS_TABLE_KEY_DISPLAY_NAME + INTEGER + COMMA
            + CONTACTs_TABLE_KEY_IS_VALIDATED + INTEGER
            +")";


    public static final class ContactsContentValueBuilder{
        private ContentValues values= new ContentValues();

        public ContactsContentValueBuilder contactUserName(String param){
            values.put(CONTACTS_TABLE_KEY_USERNAME, param);
            return this;
        }

        public ContactsContentValueBuilder contactDisplayName(String param){
            values.put(CONTACTS_TABLE_KEY_DISPLAY_NAME, param);
            return this;
        }

        public ContactsContentValueBuilder isValidated(int num){
            values.put(CONTACTs_TABLE_KEY_IS_VALIDATED, num);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
