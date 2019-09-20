package com.example.varun.vesica;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.varun.vesica.baseclass.VesicaActivity;
import com.example.varun.vesica.database.DbBriteHelper;
import com.example.varun.vesica.database.DbConstants;
import com.example.varun.vesica.database.tables.ConversationsTable;
import com.example.varun.vesica.models.Conversation;
import com.example.varun.vesica.services.SocketService;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;


public class ChatOverviewActivity extends VesicaActivity {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private BriteDatabase db;
    private List<Conversation> conversationList = new ArrayList<>();
    private boolean _loadedOnce=false;
    private Subscription subscription=null;
    @BindView(R.id.recycler_view)RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @OnClick(R.id.fab)
    void newContact(){
        myStartActivity(NewConversationActivity.class, null);
    }

    @OnClick(R.id.btn_logout)
    public void click(){
        myStartActivity(LoginActivity.class, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_overview);
        ButterKnife.bind(this);
        initResources();
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            LogD("Subscription removed!");
            subscription.unsubscribe();
        }catch (Exception e){}
    }

    @Override
    protected void onResume() {
        super.onResume();
        briteDbForRecentChatThread();
    }

    private void initResources(){
        setContext(ChatOverviewActivity.this);
        setTAG();
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("Chat Rooms");
        }
        db = DbBriteHelper.getSqlBriteDb(ChatOverviewActivity.this);
        initServiceIfClosed();

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ConversationsListAdapter(conversationList, mContext);
        mRecyclerView.setAdapter(mAdapter);
        LogD("Testing basic logging activity");

    }

    private void initServiceIfClosed(){
        //todo Does this need to be here?
        if (!isMyServiceRunning(SocketService.class)){
            LogD("Starting service");
            startService(new Intent(this, SocketService.class));
            showShortToast("Service Started");
        }else LogD("Service is already running");
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                LogD(service.service.getClassName());
                return true;
            }
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        showLongToast("Button is disabled, please logout securely instead");
    }


    // New code ////////////////////////////////////////////////////////////////////////////////////
    private void briteDbForRecentChatThread(){
        final String queryVal;
        queryVal = "SELECT * FROM "+ ConversationsTable.CONVERSATION_TABLE_NAME+ " ORDER BY id DESC";
        Observable<SqlBrite.Query> users = db.createQuery(ConversationsTable.CONVERSATION_TABLE_NAME, queryVal);
        subscription=users.observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<SqlBrite.Query>() {
            @Override public void call(SqlBrite.Query query) {
                LogD("Query called for briteDb");
                LogD(queryVal);
                Cursor cursor = query.run();
                // TODO parse data...

                if (cursor != null && cursor.moveToFirst()) {

                    if (_loadedOnce){
                        LogD(String.valueOf(_loadedOnce));
                        Conversation thread = new Conversation();
                        thread.setThreadName(DbBriteHelper.getString(cursor, ConversationsTable.CONVERSATION_TABLE_KEY_NAME));
                        thread.setThreadPk(DbBriteHelper.getLong(cursor, DbConstants.COLUMN_ID));
                        conversationList.add(thread);
                        mAdapter.notifyDataSetChanged();
                    }else {
                        do {
                            LogD(String.valueOf(_loadedOnce));
                            Conversation thread = new Conversation();
                            thread.setThreadName(DbBriteHelper.getString(cursor, ConversationsTable.CONVERSATION_TABLE_KEY_NAME));
                            thread.setThreadPk(DbBriteHelper.getLong(cursor, DbConstants.COLUMN_ID));
                            conversationList.add(thread);
                            mAdapter.notifyDataSetChanged();
                        } while (cursor.moveToNext());
                        _loadedOnce=true;
                    }

                }else LogD("No existing conversations found");
            }
        });
    }
}