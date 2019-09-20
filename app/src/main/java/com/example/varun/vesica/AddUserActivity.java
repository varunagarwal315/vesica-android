package com.example.varun.vesica;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.example.varun.vesica.baseclass.VesicaActivity;
import com.example.varun.vesica.database.DbBriteHelper;
import com.example.varun.vesica.database.DbOpenHelper;
import com.example.varun.vesica.database.tables.ContactsTable;
import com.example.varun.vesica.models.AddUserModel;
import com.example.varun.vesica.models.Contact;
import com.example.varun.vesica.network.AuthService;
import com.example.varun.vesica.preferencemanager.UserCredentialsPreference;
import com.example.varun.vesica.utils.StringUtils;
import com.squareup.sqlbrite.BriteDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.varun.vesica.database.DbConstants.SELECT_ALL_FROM;
import static com.example.varun.vesica.database.DbConstants.WHERE;

public class AddUserActivity extends VesicaActivity {

    private BriteDatabase briteDb;
    @BindView(R.id.toolbar)Toolbar toolbar;
    @BindView(R.id.et_username)EditText etUsername;
    @OnClick(R.id.btn_add_user)
    void addUserToDb(){
        addUser();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        ButterKnife.bind(this);
        initResources();
    }

    private void initResources(){
        setContext(AddUserActivity.this);
        setTAG();
        setSupportActionBar(toolbar);
        if (getSupportActionBar()!=null){
            getSupportActionBar().setTitle("New Conversation");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        briteDb = DbBriteHelper.getSqlBriteDb(mContext);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        myStartActivity(NewConversationActivity.class, null);
    }

    private void addUser(){
        //Validator
        if (StringUtils.isBlank(etUsername.getText().toString())){
            showLongToast("Username cannot be left blank!");
        }else if(etUsername.getText().toString().equals(UserCredentialsPreference.getUsername())){
            showLongToast("You cannot add your self idiot. My App will crash");
        }else {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.baseIpAddress + "3000")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            AddUserModel model= new AddUserModel();
            model.setUserName(etUsername.getText().toString());
            AuthService authService = retrofit.create(AuthService.class);
            Call<Contact> addContactCall = authService.addUserReq(model);
            addContactCall.enqueue(new Callback<Contact>() {
                @Override
                public void onResponse(Call<Contact> call, Response<Contact> response) {
                    Contact contact= response.body();
                    LogD(String.valueOf(contact.getUserFound()));
                    if (contact.getUserFound()){
                        addUserIfNewContact(contact.getUserName());

                    }else {
                        showLongToast("UserName does not exist. Please check again");
                    }
                }

                @Override
                public void onFailure(Call<Contact> call, Throwable t) {
                    LogE(t.toString());
                    showLongToast("Something went wrong! Throwable called");
                }
            });
        }
    }

    private void addUserIfNewContact(String newContactName){
        SQLiteDatabase db= new DbOpenHelper(mContext).getReadableDatabase();
        String query = SELECT_ALL_FROM
                + ContactsTable.CONTACTS_TABLE_NAME
                + WHERE + ContactsTable.CONTACTS_TABLE_KEY_USERNAME + "='" + newContactName + "'";
        Cursor c= db.rawQuery(query, null);;

        if (c.moveToFirst()){
            //User exists
            LogD("User already exists");
            showLongToast("User already added to contact list");
        }else {
            //no such user exists
            ContentValues contactCv= new ContactsTable.ContactsContentValueBuilder()
                    .contactUserName(newContactName)
                    .build();
            briteDb.insert(ContactsTable.CONTACTS_TABLE_NAME, contactCv);
            showShortToast("User added!");
            myStartActivity(NewConversationActivity.class, null);

        }

    }
}
