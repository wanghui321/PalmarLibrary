package com.example.palmarlibrary;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 52943 on 2018/5/22.
 */

public class UserSettingActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting_layout);
        ImageView imgHead = findViewById(R.id.user_setting_user_head);
        TextView userName = findViewById(R.id.user_setting_username);
        TextView cardNum = findViewById(R.id.user_setting_card_number);
        TextView realName = findViewById(R.id.user_setting_user_realname);
        TextView departName = findViewById(R.id.user_setting_department_name);
        TextView userMail = findViewById(R.id.user_setting_user_mail);
        ImageView exLogin = findViewById(R.id.user_setting_exchange_login);
    }
}
