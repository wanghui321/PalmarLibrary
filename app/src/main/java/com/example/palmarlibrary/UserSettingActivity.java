package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by 52943 on 2018/5/22.
 */

public class UserSettingActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting_layout);
        ImageView imgHead = findViewById(R.id.user_setting_user_head);
        final TextView userName = findViewById(R.id.user_setting_username);
        final TextView cardNum = findViewById(R.id.user_setting_card_number);
        final EditText realName = findViewById(R.id.user_setting_user_realname);
        final EditText departName = findViewById(R.id.user_setting_department_name);
        final EditText userMail = findViewById(R.id.user_setting_user_mail);
        final ImageView exLogin = findViewById(R.id.user_setting_exchange_login);
        ImageView back = findViewById(R.id.user_setting_back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(UserSettingActivity.this,HomePageActivity.class);
                startActivity(intent);
            }
        });

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.BASE_URL + "getUser.do")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String userStr = response.body().string();
                Log.e("user",userStr);
                Gson gson = new Gson();
                User user = gson.fromJson(userStr,User.class);
                userName.setText(user.getNickname().toString());
                cardNum.setText(user.getUserId().toString());
                if (user.getUserName() != null){
                    realName.setText(user.getUserName().toString());
                }
                if (user.getDepartment() != null){
                    departName.setText(user.getUserName().toString());
                }
                if (user.getEmail() != null){
                    userMail.setText(user.getUserName().toString());
                }
            }
        });
    }
}
