package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

/**
 * Created by 52943 on 2018/5/22.
 */

public class HomePageActivity extends Activity implements View.OnClickListener{

    private String schoolName = null;

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

        if (Constant.user.getImgUrl() != null){
            RequestOptions requestOptions = new RequestOptions().circleCrop();
            Glide.with(HomePageActivity.this)
                    .load(Constant.BASE_URL + Constant.user.getImgUrl())
                    .apply(requestOptions)
                    .into(user);
        }

        user.setOnClickListener(this);
        search.setOnClickListener(this);
        history.setOnClickListener(this);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(HomePageActivity.this,MyFavoritesActivity.class);
                startActivity(intent);
            }
        });
        readed.setOnClickListener(this);
        hotbooks.setOnClickListener(this);
        message.setOnClickListener(this);

        Intent intent = getIntent();
        schoolName = intent.getStringExtra("schoolName");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_page_search:
                Intent intent1 = new Intent(HomePageActivity.this,CollectionBookActivity.class);
                startActivity(intent1);
                break;
            case R.id.home_page_history:
                Intent intent2 = new Intent(HomePageActivity.this,BorrowingRecordsActivity.class);
                startActivity(intent2);
                break;
            case R.id.home_page_favorite:
                Intent intent3 = new Intent(HomePageActivity.this,MyFavoritesActivity.class);
                startActivity(intent3);
                break;
            case R.id.home_page_readed:
                Intent intent4 = new Intent(HomePageActivity.this,ReadBookActivity.class);
                startActivity(intent4);
                break;
            case R.id.home_page_hotbooks:
                Intent intent5 = new Intent(HomePageActivity.this,HotBookActivity.class);
                startActivity(intent5);
                break;
            case R.id.message:
                Intent intent6 = new Intent(HomePageActivity.this,AdviseActivity.class);
                startActivity(intent6);
                break;
            case R.id.home_page_user:
                Intent intent7 = new Intent(HomePageActivity.this,UserSettingActivity.class);
                intent7.putExtra("schoolName",schoolName);
                startActivity(intent7);
        }
    }
}
