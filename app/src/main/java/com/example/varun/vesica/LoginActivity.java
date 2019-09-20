package com.example.varun.vesica;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.varun.vesica.baseclass.VesicaActivity;
import com.example.varun.vesica.models.AuthRequest;
import com.example.varun.vesica.models.AuthResponse;
import com.example.varun.vesica.network.AuthService;
import com.example.varun.vesica.preferencemanager.UserCredentialsPreference;
import com.example.varun.vesica.utils.StringUtils;
import com.example.varun.vesica.utils.Utils;
import com.jakewharton.rxbinding.widget.RxTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Func2;

public class LoginActivity extends VesicaActivity {

    @BindView(R.id.et_password)EditText etPassword;
    @BindView(R.id.et_username)EditText etUserName;
    @BindView(R.id.tv_error)TextView tvError;
    @BindView(R.id.btn_chat_one)Button btnLoginOne;

    @OnClick(R.id.btn_chat_one)
    void loginTheUser(){
        //UserCredentialsPreference.setUsername(etUserName.getText().toString().trim());
        //Intent intent = new Intent(LoginActivity.this, ChatOverviewActivity.class);
        Intent intent = new Intent(LoginActivity.this, TestActivity.class);
        startActivity(intent);
    }


    private Observable<CharSequence> passWordObservable;
    private Observable<CharSequence>userNameObservable;
    private Observable<Boolean>combinedObservable;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //setSupportActionBar(toolbar);
        setContext(LoginActivity.this);
        btnLoginOne.setEnabled(false);
        passWordObservable = RxTextView.textChanges(etPassword);
        userNameObservable = RxTextView.textChanges(etUserName);
        loginUser();
        Utils.hideSoftKeyboard(LoginActivity.this);

        //todo: TEMP !!!

        showLongToast(UserCredentialsPreference.getUsername());
        if (!StringUtils.isBlank(UserCredentialsPreference.getUsername())){
            myStartActivity(ChatOverviewActivity.class, null);
        }
    }

    private void loginUser(){
        combinedObservable = Observable.combineLatest(passWordObservable, userNameObservable,
                new Func2<CharSequence, CharSequence, Boolean>() {
                    @Override
                    public Boolean call(CharSequence charSequence, CharSequence charSequence2) {
                        Log.d("TAG", charSequence.toString()+"  "+charSequence2.toString());
                        return  (!StringUtils.isBlank(charSequence)&&!StringUtils.isBlank(charSequence2));
                    }
                });

        subscription=combinedObservable.subscribe(new Observer<Boolean>() {
            @Override
            public void onCompleted() {}
            @Override
            public void onError(Throwable e) {}
            @Override
            public void onNext(Boolean aBoolean) {
                if (aBoolean){
                    btnLoginOne.setEnabled(true);
                    tvError.setText("You may now login!");
                    tvError.setTextColor(Color.parseColor("#006400"));
                }else {
                    tvError.setText("Error: password/ username cannot be blank");
                    btnLoginOne.setEnabled(false);
                    tvError.setTextColor(Color.RED);
                }
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run(){
                Utils.hideSoftKeyboard(LoginActivity.this);
            }
        }, 75);

    }


    private void tryAuthRequest(final AuthRequest authRequest){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.baseIpAddress + "5555")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final String username = authRequest.getUsername();
        AuthService authService = retrofit.create(AuthService.class);
        Call<AuthResponse>call = authService.initAuthRequest(authRequest);
        call.enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                Log.w("OnResponse ",response.body().toString());
                AuthResponse authResponse = response.body();
                if (authResponse.getStatus().equals("success")){
                    //Login successful
                    Constants.setUsername(etUserName.getText().toString());
                    Constants.setPortNumber(authResponse.getPortNumber());
                    UserCredentialsPreference.setUsername(username);
                    Intent intent = new Intent(LoginActivity.this, ChatOverviewActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Success!", Toast.LENGTH_LONG).show();
                }else {
                    //Login failed
                    Toast.makeText(LoginActivity.this, "Autherization Failed!!", Toast.LENGTH_LONG).show();
                    UserCredentialsPreference.setUsername("");
                }
            }
            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                Log.e("error ",call.toString());
                Log.e("error ",t.toString());

            }
        });
    }
}
