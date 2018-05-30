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

import java.io.IOException;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
        final String msg = intent.getStringExtra("msg");

        RequestBody requestBody = new FormBody.Builder()
                .add("indexId",indexId)
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Constant.BASE_URL + "getBookReview.do")
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws
                    IOException {
                String review = response.body().string();
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken <List<Map<String,Object>>>(){}.getType();
                final List<Map<String,Object>> dataSource = gson.fromJson(review,type);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        BookReviewAdapter bookReviewAdapter = new BookReviewAdapter(BookReviewActivity.this,
                                R.layout.book_review_item_layout,dataSource);
                        ListView listView = findViewById(R.id.book_review_list);
                        listView.setAdapter(bookReviewAdapter);
                    }
                });

            }
        });
        TextView addreview = findViewById(R.id.book_review_add);
        addreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("indexId",indexId);
                intent.putExtra("msg",msg);
                intent.setClass(BookReviewActivity.this,AddreviewActivity.class);
                startActivity(intent);
            }
        });

        ImageView reviewback = findViewById(R.id.review_back);

        reviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BookReviewActivity.this,BookDetailActivity.class);
                intent.putExtra("msg",msg);
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

            ImageView head = convertView.findViewById(R.id.book_review_head);
            TextView bookReviewUserName = convertView.findViewById(R.id.book_review_userName);
            TextView bookReviewDetail = convertView.findViewById(R.id.book_review_detail);
            TextView reviewTime = convertView.findViewById(R.id.review_time);

            if (dataSource.get(position).get("imgUrl")==null){
                Glide.with(BookReviewActivity.this)
                        .load(R.drawable.userhead)
                        .into(head);
            }else {
                String myUrl = dataSource.get(position).get("imgUrl").toString();
                Glide.with(BookReviewActivity.this)
                        .load(myUrl)
                        .into(head);
            }
            bookReviewUserName.setText(dataSource.get(position).get("nickname").toString());
            bookReviewDetail.setText(dataSource.get(position).get("content").toString());
            reviewTime.setText(dataSource.get(position).get("commentTime").toString());
            return convertView;
        }
    }
}
