package com.example.varun.vesica.services;

import android.app.NotificationManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.varun.vesica.R;
import com.example.varun.vesica.database.DbBriteHelper;
import com.example.varun.vesica.database.DbConstants;
import com.example.varun.vesica.database.DbOpenHelper;
import com.example.varun.vesica.database.tables.ContactsTable;
import com.example.varun.vesica.database.tables.MessagesTable;
import com.example.varun.vesica.models.Message;
import com.example.varun.vesica.preferencemanager.UserCredentialsPreference;
import com.example.varun.vesica.receiver.InitSocketReceiver;
import com.example.varun.vesica.sockets.SocketSingleton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.squareup.sqlbrite.BriteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Ack;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.example.varun.vesica.database.DbConstants.SELECT_ALL_FROM;
import static com.example.varun.vesica.database.DbConstants.WHERE;
import static com.example.varun.vesica.database.tables.ConversationsTable.CONVERSATION_TABLE_KEY_NAME;
import static com.example.varun.vesica.database.tables.ConversationsTable.CONVERSATION_TABLE_NAME;
import static com.example.varun.vesica.database.tables.ConversationsTable.ConversationContentValueBuilder;


public class SocketService extends Service {

    private Socket mSocket;
    private BriteDatabase briteDb;
    private DbOpenHelper dbOpenHelper;
    private static final String TAG="SocketService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"SOCKET SERVICE WAS CALLED");
        Toast.makeText(SocketService.this, "SOCKET SERVICE STARTED!!", Toast.LENGTH_LONG).show();
        dbOpenHelper= new DbOpenHelper(SocketService.this);
        mSocket=SocketSingleton.getSocket("3000");

        mSocket.on(Socket.EVENT_CONNECT,onConnect);
        mSocket.on("new_message", onNewMessage);
//        mSocket.on(Socket.EVENT_DISCONNECT,onDisconnect);
//        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
//        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectionTimeout);

        mSocket.connect();
        briteDb = DbBriteHelper.getSqlBriteDb(SocketService.this);
    }


    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d(TAG,"connected");
            JsonObject jsonObject= new JsonObject();
            jsonObject.addProperty("userName", UserCredentialsPreference.getUsername());
            try {
                JSONObject object= new JSONObject(jsonObject.toString());
                mSocket.emit("new_user", object, newUserAck());
                Log.d(TAG, object.toString());
                Log.d(TAG, "Socket Id "+String.valueOf(mSocket.id()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    private Ack newUserAck(){
        return new Ack(){
            @Override
            public void call(Object... args) {
                Object error= args[0];
                Object success=args[1];
                if (error==null){
                    Log.d(TAG, args[1].toString());
                }else {
                }
            }
        };
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //TODO: break socket.io connection. Clean up briteDb. Check socket id
        Log.d(TAG, "Socket service closed in onDestroy()");
        mSocket.off(Socket.EVENT_CONNECT);
        mSocket.off(Socket.EVENT_DISCONNECT);
        mSocket.off(Socket.EVENT_CONNECT_ERROR);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT);
        mSocket.off("new_message");
        mSocket.off("new_user");
        mSocket.off("disconnect_user");
        mSocket.disconnect();

        sendBroadcast(new Intent(SocketService.this, InitSocketReceiver.class));
        //
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            //TODO: Decide if we keep it as this or receive JSON objects directly
            Log.d(TAG,args[0].toString());
            JSONObject messageObject = (JSONObject) args[0];
            Gson gson = new Gson();
            Message message = gson.fromJson(String.valueOf(messageObject), Message.class);
            checkIfThreadAlreadyExists(message);
        }
    };


    private void checkIfThreadAlreadyExists(Message message){
        String threadName = message.getSenderName();
        String query= SELECT_ALL_FROM+ CONVERSATION_TABLE_NAME+ WHERE+ CONVERSATION_TABLE_KEY_NAME+  "='" + threadName + "'";
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor cursor=db.rawQuery(query, null);
        long idOfChatThread;
        if (cursor.moveToFirst()){
            //Thread exists
            idOfChatThread= DbBriteHelper.getInt(cursor, DbConstants.COLUMN_ID);


        }else {
            //Create new thread. Get thread name from the chat message. Parse and Add
            idOfChatThread = createNewConversation(threadName);

        }
        cursor.close();
        checkIfUserExists(message, idOfChatThread);
    }

    private void checkIfUserExists(Message message, Long chatThreadPk){
        String user= message.getSenderName();
        String query = SELECT_ALL_FROM
                + ContactsTable.CONTACTS_TABLE_NAME
                + WHERE + ContactsTable.CONTACTS_TABLE_KEY_USERNAME + "='" + user + "'";
        SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
        Cursor c= db.rawQuery(query, null);

        if (c.moveToFirst()){
            //User exists
            long idOfUser= DbBriteHelper.getInt(c, DbConstants.COLUMN_ID);
            createMessageAndAddToTable(message, chatThreadPk, idOfUser);
        }else {
            //no such user exists
            ContentValues values= new ContactsTable.ContactsContentValueBuilder()
                    .contactUserName(user)
                    .build();
            long idOfUser= briteDb.insert(ContactsTable.CONTACTS_TABLE_NAME, values);
            createMessageAndAddToTable(message, chatThreadPk, idOfUser);
        }
    }
    //Working fine. This function is complete
    private long createNewConversation(String newThreadName){
        ContentValues values = new ConversationContentValueBuilder()
                .conversationName(newThreadName)
                .build();

        return briteDb.insert(CONVERSATION_TABLE_NAME, values);
        //todo: Can we generate a call back here?
    }

    //Final step in handling a new_message event
    private void createMessageAndAddToTable(Message message, long threadPrimaryKey, long userPk){

        ContentValues contentVal = new MessagesTable.MessageConventValueBuilder()
                .cipherText(message.getCipherText())
                .recipientName(message.getRecipientName())
                .senderName(message.getSenderName())
                .burnable(message.getBurnable())
                .burnTime(message.getBurnTime())
                .read(0)
                .isExpired(0)
                .receivedTime(System.currentTimeMillis())
                .contactForeignKey(userPk)
                .conversationForeignKey(threadPrimaryKey)
                .build();

        long idOfMessage = briteDb.insert(MessagesTable.MESSAGES_TABLE_NAME, contentVal);
        Log.d(TAG, String.valueOf(idOfMessage));
    }


    private long createUserAndReturnPk(Message message){
        ContentValues values= new ContactsTable.ContactsContentValueBuilder()
                .contactUserName(message.getSenderName())
                .build();
        return briteDb.insert(ContactsTable.CONTACTS_TABLE_NAME, values);
    }

    //This function is complete for all practical purposes
    private void initNotification(String heading, String message){
        NotificationCompat.Builder builder;
        Log.w(TAG,"creating notification");
        builder=new NotificationCompat.Builder(SocketService.this);
        builder.setContentTitle(heading);
        builder.setContentText(message);
        builder.setPriority(2);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        NotificationManager mNotifyMgr =
                (NotificationManager)SocketService.this.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotifyMgr.notify(001, builder.build());
    }

    //Make sure the service is re-created asap (if it is forced to close for any reason)
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        super.onStartCommand(intent, flags, startId);
//        return START_STICKY;
//    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void sendBroadcast(Intent intent, String receiverPermission) {
        super.sendBroadcast(intent, receiverPermission);
    }

    public SocketService() {
    }
}
