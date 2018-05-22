package com.example.palmarlibrary;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 52943 on 2018/5/22.
 */

public class HotBookActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot_book_layout);
        ImageView back = findViewById(R.id.hot_book_back);
        TextView recommend = findViewById(R.id.hot_book_recommend);
    }
}
