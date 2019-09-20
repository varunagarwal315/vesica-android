package com.example.varun.vesica;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.varun.vesica.baseclass.VesicaActivity;
import com.example.varun.vesica.database.DbBriteHelper;
import com.example.varun.vesica.database.tables.ContactsTable;
import com.example.varun.vesica.models.Contact;
import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.example.varun.vesica.database.DbConstants.ORDER_BY;
import static com.example.varun.vesica.database.DbConstants.SELECT_ALL_FROM;

public class NewConversationActivity extends VesicaActivity {

    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<Contact> contactList= new ArrayList<>();
    private BriteDatabase briteDb;
    @BindView(R.id.recycler_view)RecyclerView mRecyclerView;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @OnClick(R.id.fab)

    void addNewUser(){
        myStartActivity(AddUserActivity.class, null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conversation);
        ButterKnife.bind(this);
        initResources();
    }

    private void initResources(){
        setContext(NewConversationActivity.this);
        setTAG();
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("New Conversation");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter= new NewConversationAdapter(contactList, mContext);
        mRecyclerView.setAdapter(mAdapter);
        briteDb = DbBriteHelper.getSqlBriteDb(mContext);
        initBriteDbForUserList();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myStartActivity(ChatOverviewActivity.class, null);
    }

    private void initBriteDbForUserList(){
        String queryVal= SELECT_ALL_FROM+ ContactsTable.CONTACTS_TABLE_NAME+ORDER_BY+"id DESC";
        Observable<SqlBrite.Query> usersList = briteDb.createQuery(ContactsTable.CONTACTS_TABLE_NAME, queryVal);
        usersList.observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<SqlBrite.Query>() {
                    @Override
                    public void call(SqlBrite.Query query) {
                        Cursor cursor = query.run();
                        if (cursor!=null && cursor.moveToFirst()){
                            do {
                                Contact contact= new Contact();
                                contact.setUserName(DbBriteHelper.getString(cursor, ContactsTable.CONTACTS_TABLE_KEY_USERNAME));
                                contactList.add(contact);
                            }
                            while (cursor.moveToNext());
                            mAdapter.notifyDataSetChanged();

                        }else {
                            //No users there. Do something !! TODO:
                            showLongToast("No contacts stored");
                        }

                    }
                });
    }
}
