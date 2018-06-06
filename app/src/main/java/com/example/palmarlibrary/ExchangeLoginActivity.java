package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.io.IOException;
import java.lang.reflect.*;
import java.util.Map;

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

public class ExchangeLoginActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_login_layout);
        final TextView cardNum = findViewById(R.id.exchange_login_card_number);
        final TextView newPassword = findViewById(R.id.exchange_login_password);
        final TextView error = findViewById(R.id.ex_login_error);
        Button login = findViewById(R.id.ex_btn_login);
        Button reset = findViewById(R.id.ex_btn_reset);
        Button regist = findViewById(R.id.ex_btn_regist);

        Intent intent = getIntent();
        final String schoolName = intent.getStringExtra("schoolName");

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardNum.setText("");
                newPassword.setText("");
            }
        });

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("schoolName",schoolName);
                intent.setClass(ExchangeLoginActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestBody requestBody = new FormBody.Builder()
                        .add("schoolName",schoolName)
                        .add("userId",cardNum.getText().toString())
                        .add("password",newPassword.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .post(requestBody)
                        .url(Constant.BASE_URL + "login.do")
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
                        Log.e("Login",msg);
                        if (msg.equals("fail")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    error.setText("借书卡号或密码错误！");
                                }
                            });

                        } else if (msg.equals("noUser")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    error.setText("用户还未注册，请先注册！");
                                }
                            });
                        } else{
                            User user = Constant.user;
                            Gson gson = new Gson();
                            Log.e("Login",msg);
                            Type type = new TypeToken<Map<String,Object>>(){}.getType();
                            Map <String,Object> map = gson.fromJson(msg,type);
                            user.setUserId(cardNum.getText().toString());
                            user.setNickname(map.get("nickname").toString());
                            if (map.get("userName") != null) {
                                user.setUserName(map.get("userName").toString());
                            }
                            if (map.get("department")!=null){
                                user.setDepartment(map.get("department").toString());
                            }
                            if (map.get("email")!=null){
                                user.setEmail(map.get("email").toString());
                            }
                            if (map.get("imgUrl") != null){
                                user.setImgUrl(map.get("imgUrl").toString());
                            }
                            Intent intent = new Intent();
                            intent.setClass(ExchangeLoginActivity.this,
                                    HomePageActivity.class);
                            intent.putExtra("schoolName",schoolName);
                            startActivity(intent);
                        }
                    }
                });
            }
        });
    }
}
