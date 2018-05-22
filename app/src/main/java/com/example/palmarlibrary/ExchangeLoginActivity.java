package com.example.palmarlibrary;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
    }
}
