package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by 52943 on 2018/5/22.
 */

public class ExchangeLoginActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exchange_login_layout);
        TextView cardNum = findViewById(R.id.exchange_login_card_number);
        TextView newPassword = findViewById(R.id.exchange_login_password);
        Button login = findViewById(R.id.ex_btn_login);
        Button reset = findViewById(R.id.ex_btn_reset);
        Button backlogin = findViewById(R.id.backlogin);

        backlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ExchangeLoginActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(ExchangeLoginActivity.this,choseprovinceActivity.class);
                startActivity(intent);
            }
        });
    }
}
