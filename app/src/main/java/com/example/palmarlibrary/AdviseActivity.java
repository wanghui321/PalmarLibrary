package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
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
 * Created by ruiwang on 2018/5/22.
 */

public class AdviseActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advise_layout);

        ImageView adviseback = findViewById(R.id.advise_back);
        Button advisesubmit = findViewById(R.id.advise_submit);
        final TextView advisetv = findViewById(R.id.et_advise);

        advisesubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String advise = advisetv.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("advise",advise);
                intent.setClass(AdviseActivity.this,AdviseSubmitLoadingActivity.class);
                startActivity(intent);
            }
        });

        adviseback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AdviseActivity.this,HomePageActivity.class);
                startActivity(intent);
            }
        });

    }
}
