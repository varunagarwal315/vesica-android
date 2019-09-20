package com.example.varun.vesica.database.tables;

import android.content.ContentValues;
import android.support.annotation.NonNull;

import static com.example.varun.vesica.database.DbConstants.COLUMN_ID;
import static com.example.varun.vesica.database.DbConstants.COMMA;
import static com.example.varun.vesica.database.DbConstants.CREATE_TABLE;
import static com.example.varun.vesica.database.DbConstants.INTEGER;
import static com.example.varun.vesica.database.DbConstants.PRIMARY_KEY;
import static com.example.varun.vesica.database.DbConstants.TEXT;

/**
 * Created by varun on 10/10/16.
 */
public class MessagesTable {

    public static final String MESSAGES_TABLE_NAME ="message_table_name";
    public static final String MESSAGE_KEY_CIPHER_TEXT ="message_table_key_c";
    public static final String MESSAGE_KEY_SENDER_NAME ="message_table_key_sender";
    public static final String MESSAGE_KEY_RECIPIENT_NAME ="message_table_key_receiver";
    public static final String MESSAGE_KEY_BURNABLE="message_table_key_burnable";
    public static final String MESSAGE_KEY_BURN_TIME="message_table_key_burn_time";
    public static final String MESSAGE_kEY_EXPIRED= "message_table_key_expired";
    public static final String MESSAGE_KEY_DELIVERY_TIME="message_table_key_delivery_time";
    public static final String MESSAGE_KEY_RECEIVED_TIME="message_table_key_received_time";
    public static final String MESSAGE_KEY_READ= "message_table_key_read";
    public static final String MESSAGE_KEY_MESSAGE_ID="message_table_key_id";

    public static final String MESSAGE_KEY_CONVERSATION_FK="message_key_conversation_fk";
    public static final String MESSAGE_KEY_CONTACT_FK="message_key_contact_fk";


    public static final String CREATE_TABLE_CHAT_MESSAGES = CREATE_TABLE + MESSAGES_TABLE_NAME +
            "("
            + COLUMN_ID + PRIMARY_KEY + COMMA
            + MESSAGE_KEY_CIPHER_TEXT + TEXT + COMMA
            + MESSAGE_KEY_SENDER_NAME + TEXT + COMMA
            + MESSAGE_KEY_RECIPIENT_NAME + TEXT + COMMA
            + MESSAGE_KEY_BURNABLE + INTEGER + COMMA
            + MESSAGE_KEY_BURN_TIME + INTEGER + COMMA
            + MESSAGE_kEY_EXPIRED + INTEGER + COMMA
            + MESSAGE_KEY_DELIVERY_TIME + INTEGER + COMMA
            + MESSAGE_KEY_RECEIVED_TIME + INTEGER + COMMA
            + MESSAGE_KEY_READ + INTEGER + COMMA
            + MESSAGE_KEY_MESSAGE_ID + INTEGER + COMMA
            + MESSAGE_KEY_CONVERSATION_FK + INTEGER + COMMA
            + MESSAGE_KEY_CONTACT_FK + INTEGER
            +")";


    public static final class MessageConventValueBuilder{
        private ContentValues values= new ContentValues();

        public MessageConventValueBuilder cipherText(@NonNull String cipherText) {
            values.put(MESSAGE_KEY_CIPHER_TEXT, cipherText);
            return this;
        }

        public MessageConventValueBuilder senderName(@NonNull String sender) {
            values.put(MESSAGE_KEY_SENDER_NAME, sender);
            return this;
        }

        public MessageConventValueBuilder recipientName(@NonNull String recipientName) {
            values.put(MESSAGE_KEY_RECIPIENT_NAME, recipientName);
            return this;
        }

        public MessageConventValueBuilder burnable(int param) {
            values.put(MESSAGE_KEY_BURNABLE, param);
            return this;
        }

        public MessageConventValueBuilder burnTime(long burnTIme) {
            values.put(MESSAGE_KEY_BURN_TIME, burnTIme);
            return this;
        }

        public MessageConventValueBuilder isExpired(int isExpired) {
            values.put(MESSAGE_kEY_EXPIRED, isExpired);
            return this;
        }

        public MessageConventValueBuilder deliveryTime(long deliveryTime) {
            values.put(MESSAGE_KEY_DELIVERY_TIME, deliveryTime);
            return this;
        }

        public MessageConventValueBuilder receivedTime(long receivedTime) {
            values.put(MESSAGE_KEY_RECIPIENT_NAME, receivedTime);
            return this;
        }

        public MessageConventValueBuilder read(int read) {
            values.put(MESSAGE_KEY_READ, read);
            return this;
        }

        public MessageConventValueBuilder messageId( long messageId) {
            values.put(MESSAGE_KEY_MESSAGE_ID, messageId);
            return this;
        }

        public MessageConventValueBuilder conversationForeignKey(long key) {
            values.put(MESSAGE_KEY_CONVERSATION_FK, key);
            return this;
        }

        public MessageConventValueBuilder contactForeignKey(long key) {
            values.put(MESSAGE_KEY_CONTACT_FK, key);
            return this;
        }

        public ContentValues build() {
            return values;
        }

    }

}
