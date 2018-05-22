package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

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

        advisesubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(AdviseActivity.this,AdviseSubmitLoadingActivity.class);
                startActivity(intent);
            }
        });

        adviseback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //intent.setClass(AdviseActivity.this,HomePageActivity.class);
                startActivity(intent);
            }
        });

    }
}
