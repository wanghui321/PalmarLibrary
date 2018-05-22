package com.example.palmarlibrary;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by 52943 on 2018/5/22.
 */

public class HomePageActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_layout);
        ImageView user = findViewById(R.id.home_page_user);
        ImageView schoolImg = findViewById(R.id.home_page_school_image);
        LinearLayout search = findViewById(R.id.home_page_search);
        LinearLayout history = findViewById(R.id.home_page_history);
        LinearLayout favorite = findViewById(R.id.home_page_favorite);
        LinearLayout readed = findViewById(R.id.home_page_readed);
        LinearLayout hotbooks = findViewById(R.id.home_page_hotbooks);
        LinearLayout message = findViewById(R.id.message);

    }
}
