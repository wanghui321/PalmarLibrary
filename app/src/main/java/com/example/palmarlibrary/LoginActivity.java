package com.example.palmarlibrary;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by 52943 on 2018/5/22.
 */

public class LoginActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        TextView userName = findViewById(R.id.login_name);
        TextView userPassword = findViewById(R.id.login_password);
        TextView cardNum = findViewById(R.id.login_card_number);
        Button login = findViewById(R.id.btn_login);
        Button reset = findViewById(R.id.btn_reset);
    }
}
