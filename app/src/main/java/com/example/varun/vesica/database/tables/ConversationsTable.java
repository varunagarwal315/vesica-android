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
public class ConversationsTable {

    public static final String CONVERSATION_TABLE_NAME ="conversation_table_name";
    public static final String CONVERSATION_TABLE_KEY_NAME ="conversation_table_key_name";
    public static final String CONVERSATION_TABLE_UNREAD_MESSAGE_COUNT="conversation_table_key_unread_count";
    public static final String CONVERSATION_TABLE_KEY_EXCHANGE_COMPLETED="conversation_table_name_key_exchange_completed";


    public static final String CREATE_TABLE_CONVERSATIONS = CREATE_TABLE + CONVERSATION_TABLE_NAME+
            "("
            + COLUMN_ID + PRIMARY_KEY + COMMA
            + CONVERSATION_TABLE_KEY_NAME + TEXT + COMMA
            + CONVERSATION_TABLE_KEY_EXCHANGE_COMPLETED + INTEGER + COMMA
            + CONVERSATION_TABLE_UNREAD_MESSAGE_COUNT + INTEGER
            +")";

    public static final class ConversationContentValueBuilder{
        private ContentValues values= new ContentValues();

        public ConversationContentValueBuilder conversationName(String param){
            values.put(CONVERSATION_TABLE_KEY_NAME, param);
            return this;
        }

        public ConversationContentValueBuilder unreadMessageCount(Integer num){
            values.put(CONVERSATION_TABLE_UNREAD_MESSAGE_COUNT, num);
            return this;
        }

        public ConversationContentValueBuilder keyExchangeComplete(int param){
            values.put(CONVERSATION_TABLE_KEY_EXCHANGE_COMPLETED, param);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }
}
