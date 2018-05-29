package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/16.
 */

public class BookDetailActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_layout);

        Button btnReview = findViewById(R.id.btn_book_detail_review);
        Button btnSelect = findViewById(R.id.btn_book_detail_select);
        ImageView detailback = findViewById(R.id.detail_back);
        final ImageView collect = findViewById(R.id.book_detail_collect);
        TextView bookname = findViewById(R.id.book_detail_bookName);
        TextView author = findViewById(R.id.book_detail_author);
        TextView booknumber = findViewById(R.id.book_detail_bookNum);
        TextView publish = findViewById(R.id.book_detail_publish);
        TextView price = findViewById(R.id.book_detail_price);
        TextView detailtype = findViewById(R.id.book_detail_type);
        TextView congbianxiang = findViewById(R.id.book_detail_congbianxiang);
        TextView topic = findViewById(R.id.book_detail_topic);

        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取当前图片ConstantState类对象
                Drawable.ConstantState t1 = collect.getDrawable().getCurrent().getConstantState();
                //找到需要比较的图片ConstantState类对象
                //Drawable.ConstantState t2 = ContextCompat.getDrawable(BookDetailActivity.this,R.drawable.collect).getConstantState();
                Drawable.ConstantState t2 = getResources().getDrawable(R.drawable.collect).getConstantState();
                if(t1.equals(t2))
                {
                    collect.setImageResource(R.drawable.collected);
                }
                else{
                    collect.setImageResource(R.drawable.collect);
                }
            }
        });

        detailback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BookDetailActivity.this,BookReviewActivity.class);
                startActivity(intent);
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BookDetailActivity.this,BookReviewActivity.class);
                startActivity(intent);
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BookDetailActivity.this,CollectionBookActivity.class);
                startActivity(intent);
            }
        });

        String bookdetailstr = getIntent().getStringExtra("bookdetailstr");
        Gson gson = new Gson();
        List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
        Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
        list = gson.fromJson(bookdetailstr,type);

        bookname.setText(list.get(0).get("bookname").toString());
        author.setText(list.get(0).get("author").toString());
        booknumber.setText(list.get(0).get("booknumber").toString());
        publish.setText(list.get(0).get("publish").toString());
        price.setText(list.get(0).get("price").toString());
        detailtype.setText(list.get(0).get("detailtype").toString());
        congbianxiang.setText(list.get(0).get("congbianxiang").toString());
        topic.setText(list.get(0).get("topic").toString());
    }
}
