package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 52943 on 2018/5/22.
 */

public class LoginActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        final TextView userName = findViewById(R.id.login_name);
        final TextView userPassword = findViewById(R.id.login_password);
        final TextView cardNum = findViewById(R.id.login_card_number);
        final TextView error = findViewById(R.id.login_error);
        Button login = findViewById(R.id.btn_login);
        final Button reset = findViewById(R.id.btn_reset);
        Button gotoexchange = findViewById(R.id.go_exchange_login);

        Intent intent = getIntent();
        final String schoolName = intent.getStringExtra("schoolName");

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userName.setText("");
                userPassword.setText("");
                cardNum.setText("");
            }
        });

        gotoexchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,ExchangeLoginActivity.class);
                intent.putExtra("schoolName",schoolName);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody requestBody = new FormBody.Builder()
                        .add("schoolName",schoolName)
                        .add("nickname",userName.getText().toString())
                        .add("userId",cardNum.getText().toString())
                        .add("password",userPassword.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .post(requestBody)
                        .url(Constant.BASE_URL + "regist.do")
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String msg = response.body().string();
                        if (msg.equals("success")){
                            User user = Constant.user;
                            user.setUserId(cardNum.getText().toString());
                            user.setNickname(userName.getText().toString());

                            SharedPreferences preferences = getSharedPreferences("userData",
                                    MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("userId",cardNum.getText().toString());
                            editor.putString("password",userPassword.getText().toString());
                            editor.commit();

                            Intent intent = new Intent();
                            intent.setClass(LoginActivity.this,HomePageActivity.class);
                            intent.putExtra("schoolName",schoolName);
                            startActivity(intent);
                        } else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    error.setText("借书卡号或密码错误");
                                    userName.setText("");
                                    userPassword.setText("");
                                    cardNum.setText("");
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
