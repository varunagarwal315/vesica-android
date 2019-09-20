package com.example.varun.vesica;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.varun.vesica.baseclass.VesicaActivity;
import com.example.varun.vesica.database.DbBriteHelper;
import com.example.varun.vesica.database.tables.ConversationsTable;
import com.example.varun.vesica.database.tables.MessagesTable;
import com.example.varun.vesica.models.Message;
import com.example.varun.vesica.preferencemanager.UserCredentialsPreference;
import com.example.varun.vesica.sockets.SocketSingleton;
import com.example.varun.vesica.utils.StringUtils;
import com.google.gson.Gson;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.socket.client.Socket;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.example.varun.vesica.database.DbConstants.EQUALS;
import static com.example.varun.vesica.database.DbConstants.SELECT_ALL_FROM;
import static com.example.varun.vesica.database.DbConstants.WHERE;
import static com.example.varun.vesica.database.tables.MessagesTable.MESSAGES_TABLE_NAME;
import static com.example.varun.vesica.database.tables.MessagesTable.MESSAGE_KEY_BURNABLE;
import static com.example.varun.vesica.database.tables.MessagesTable.MESSAGE_KEY_BURN_TIME;
import static com.example.varun.vesica.database.tables.MessagesTable.MESSAGE_KEY_CIPHER_TEXT;
import static com.example.varun.vesica.database.tables.MessagesTable.MESSAGE_KEY_CONVERSATION_FK;
import static com.example.varun.vesica.database.tables.MessagesTable.MESSAGE_KEY_RECIPIENT_NAME;
import static com.example.varun.vesica.database.tables.MessagesTable.MESSAGE_KEY_SENDER_NAME;

public class ConversationActivity extends VesicaActivity {
    private Socket mSocketOne;
    private Gson gson = new Gson();
    private List<Message> messageList = new ArrayList<Message>();
    private String userName;
    private String recipientName;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BriteDatabase briteDb;
    private Long pkId;
    private boolean _loadedOnce=false;

    @BindView(R.id.et_text)EditText etText;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.et_self_destruct_time)EditText etSelfDestructTimer;
    @BindView(R.id.cb_self_destruct)CheckBox cbSelfDestruct;
    @BindView(R.id.recycler_view)RecyclerView mRecyclerView;

    @OnClick(R.id.btn_send)
    void sendMessage(){
        if (!StringUtils.isBlank(etText.getText().toString())){
            Message message;
            message= new Message.MessageBuilder()
                    .cipherText(etText.getText().toString())
                    .deliveryTime(System.currentTimeMillis())
                    .messageId(123456)
                    .isExpired(0)
                    .senderName(userName)
                    .recipientName(recipientName)
                    .build();
            if (cbSelfDestruct.isChecked() && !StringUtils.isBlank(etSelfDestructTimer.getText().toString())){
                message.setBurnable(1);
                message.setBurnTime(Long.parseLong(etSelfDestructTimer.getText().toString()));
            }else {
                message.setBurnTime(0);
                message.setBurnable(0);
            }

            String jsonString= gson.toJson(message);
            try {
                JSONObject object = new JSONObject(jsonString);
                mSocketOne.emit("new_message",object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //Add message to the database now
            ContentValues values= new MessagesTable.MessageConventValueBuilder()
                    .cipherText(etText.getText().toString())
                    .deliveryTime(System.currentTimeMillis())
                    .messageId(123456)
                    .isExpired(0)
                    .senderName(userName)
                    .recipientName(recipientName)
                    .conversationForeignKey(pkId)
                    .build();
            if (cbSelfDestruct.isChecked() && !StringUtils.isBlank(etSelfDestructTimer.getText().toString())){
                message.setBurnTime(Long.parseLong(etSelfDestructTimer.getText().toString()));
                values.put(MESSAGE_KEY_BURNABLE, 1);
                values.put(MESSAGE_KEY_BURN_TIME, Long.parseLong(etSelfDestructTimer.getText().toString()));
            }else {
                values.put(MESSAGE_KEY_BURNABLE, 0);
                values.put(MESSAGE_KEY_BURN_TIME, 0);
            }

            briteDb.insert(MESSAGES_TABLE_NAME, values);
            etText.setText("");

        }else {
            //User tried to send blank message. Allow???
        }
    }


    @OnClick(R.id.cb_self_destruct)
    void toggleText(){
        if (cbSelfDestruct.isChecked()) {
            etSelfDestructTimer.setEnabled(true);
            etSelfDestructTimer.setHint("Time in seconds");
        }
        else {
            etSelfDestructTimer.setEnabled(false);
            etSelfDestructTimer.setHint("Disabled");
        }
    }

    @OnClick(R.id.btn_wipe_history)
    void clearHistory(){
        showDialogBoxToClearHistory();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initResources();
    }

    private void initResources(){
        briteDb = DbBriteHelper.getSqlBriteDb(ConversationActivity.this);
        setSupportActionBar(toolbar);
        setContext(ConversationActivity.this);
        setTAG();
        if (getSupportActionBar()!=null) getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        userName = UserCredentialsPreference.getUsername();
        LogD(userName);
        mSocketOne= SocketSingleton.getSocket("3000");
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ChatAdapter(messageList, userName);
        mRecyclerView.setAdapter(mAdapter);
        initIntentParser();
    }


    private void initIntentParser(){
        LogD("Intent parser");
        Intent intent= getIntent();
        recipientName=intent.getStringExtra("recipentName");
        if (intent.getStringExtra("type").equals("newConversation")){
            String conversationName = recipientName;
            ContentValues values= new ConversationsTable.ConversationContentValueBuilder()
                    .conversationName(conversationName)
                    .unreadMessageCount(0)
                    .build();
            pkId= briteDb.insert(ConversationsTable.CONVERSATION_TABLE_NAME, values);
        }else {
            pkId= intent.getExtras().getLong("PkId");
        }
        //Should be executed after this to prevent @NullPointerException
        initBriteDatabase();
    }


    //Primary functions for displaying the messages
    private void initBriteDatabase(){
        String query= SELECT_ALL_FROM+ MESSAGES_TABLE_NAME + WHERE + MESSAGE_KEY_CONVERSATION_FK+ EQUALS+ pkId+ " ORDER BY id DESC";;
        LogD(query);
        Observable<SqlBrite.Query> users = briteDb.createQuery(MESSAGES_TABLE_NAME, query);
        users.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<SqlBrite.Query>() {
            @Override public void call(SqlBrite.Query query) {
                Log.d("SQL BRITE ","query called");
                Cursor cursor = query.run();
                // TODO parse data...
                if (cursor != null && cursor.moveToFirst()) {
                    if (_loadedOnce){
                        Message message= new Message.MessageBuilder()
                                .recipientName(DbBriteHelper.getString(cursor, MESSAGE_KEY_RECIPIENT_NAME))
                                .senderName(DbBriteHelper.getString(cursor, MESSAGE_KEY_SENDER_NAME))
                                .cipherText(DbBriteHelper.getString(cursor, MESSAGE_KEY_CIPHER_TEXT))
                                .burnable(DbBriteHelper.getInt(cursor, MESSAGE_KEY_BURNABLE))
                                .burnTime(DbBriteHelper.getLong(cursor, MESSAGE_KEY_BURN_TIME))
                                .build();
                        messageList.add(message);
                        mAdapter.notifyDataSetChanged();
                        scrollToBottom();
                    }else {
                        do {
                            Message message= new Message.MessageBuilder()
                                    .recipientName(DbBriteHelper.getString(cursor, MESSAGE_KEY_RECIPIENT_NAME))
                                    .senderName(DbBriteHelper.getString(cursor, MESSAGE_KEY_SENDER_NAME))
                                    .cipherText(DbBriteHelper.getString(cursor, MESSAGE_KEY_CIPHER_TEXT))
                                    .burnable(DbBriteHelper.getInt(cursor, MESSAGE_KEY_BURNABLE))
                                    .burnTime(DbBriteHelper.getLong(cursor, MESSAGE_KEY_BURN_TIME))
                                    .build();
                            messageList.add(message);
                            mAdapter.notifyDataSetChanged();
                            scrollToBottom();
                        } while (cursor.moveToNext());
                        _loadedOnce=true;
                    }

                }else LogD("CURSOR IS NULL!");

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myStartActivity(ChatOverviewActivity.class, null);
    }

    private void scrollToBottom(){
        mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
    }

    public void showDialogBoxToClearHistory(){
        new AlertDialog.Builder(ConversationActivity.this)
                .setTitle("Wipe Chat History")
                .setMessage("This will delete the entire chat history of this thread. It is irreversible. Do you want to continue?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        messageList.clear();
                        mAdapter.notifyDataSetChanged();
                        //TODO: DELETE THIS STUFF FROM BRITE DB AS WELL
                        removeMessagesFromDb();
                        Toast.makeText(ConversationActivity.this, "Cleared entire history", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            dialog.dismiss();
                        }catch (Exception e){e.printStackTrace();}
                    }
                })
                .setCancelable(false)
                .show();
    }

    private void removeMessagesFromDb(){
        String query= SELECT_ALL_FROM+ MESSAGES_TABLE_NAME + WHERE + MESSAGE_KEY_CONVERSATION_FK+ EQUALS+ pkId+ " ORDER BY id DESC";;
        LogD(query);
        briteDb.delete(MESSAGES_TABLE_NAME, MESSAGE_KEY_CONVERSATION_FK+ EQUALS+ pkId, null);
    }
}