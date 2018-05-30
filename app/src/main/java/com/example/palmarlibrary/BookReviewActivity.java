package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/16.
 */

public class BookReviewActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_review_layout);

        Intent intent = getIntent();
        final String indexId = intent.getStringExtra("indexId");
        String review = intent.getStringExtra("review");
        Gson gson = new Gson();
        java.lang.reflect.Type type = new TypeToken <List<Map<String,Object>>>(){}.getType();
        List<Map<String,Object>> dataSource = gson.fromJson(review,type);

        TextView addreview = findViewById(R.id.book_review_add);

        addreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("indexId",indexId);
                intent.setClass(BookReviewActivity.this,AddreviewActivity.class);
                startActivity(intent);
            }
        });

        BookReviewAdapter bookReviewAdapter = new BookReviewAdapter(this,
                R.layout.book_review_item_layout,dataSource);
        ListView listView = findViewById(R.id.book_review_list);
        listView.setAdapter(bookReviewAdapter);

        ImageView reviewback = findViewById(R.id.review_back);

        reviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BookReviewActivity.this,BookDetailActivity.class);
                startActivity(intent);
            }
        });

    }

    public class BookReviewAdapter extends BaseAdapter{
        private Context context;
        private int item_layout_id;
        private List<Map<String,Object>> dataSource;

        public BookReviewAdapter (Context context,int item_layout_id,List<Map<String,Object>> dataSource){
            this.context = context;
            this.item_layout_id = item_layout_id;
            this.dataSource = dataSource;
        }

        @Override
        public int getCount() {
            return dataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return dataSource.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(item_layout_id,null);
            }

            ImageView head = findViewById(R.id.book_review_head);
            TextView bookReviewUserName = convertView.findViewById(R.id.book_review_userName);
            TextView bookReviewDetail = convertView.findViewById(R.id.book_review_detail);

            if (dataSource.get(position).get("userhead").toString()!=null){
                head.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.userhead));
            }else {
                String myUrl = dataSource.get(position).toString();
                Glide.with(BookReviewActivity.this)
                        .load(myUrl)
                        .into(head);
            }
            bookReviewUserName.setText(dataSource.get(position).get("userName").toString());
            bookReviewDetail.setText(dataSource.get(position).get("detail").toString());
            return convertView;
        }
    }
}
