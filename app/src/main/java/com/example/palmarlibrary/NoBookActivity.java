package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Administrator on 2018/6/20.
 */

public class NoBookActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_book_layout);

        ImageView back = findViewById(R.id.no_book_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(NoBookActivity.this,CollectionBookActivity.class);
                startActivity(intent);
            }
        });
    }
}