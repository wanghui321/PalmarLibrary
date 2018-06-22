package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
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

public class BookDetailActivity extends Activity {

    private boolean flag1 = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_detail_layout);

        Intent intent = getIntent();
        final String msg = intent.getStringExtra("msg");
        final String flag = intent.getStringExtra("flag");
        final String authorStr = intent.getStringExtra("author");
        final String typeName = intent.getStringExtra("typeName");
        final String bookListStr = intent.getStringExtra("bookListStr");
        final String likeAuthor = intent.getStringExtra("likeAuthor");
        Gson gson = new Gson();
        Type type = new TypeToken<Map<String,Object>>(){}.getType();
        final Map<String,Object> map = gson.fromJson(msg,type);

        Button btnReview = findViewById(R.id.btn_book_detail_review);
        Button btnSelect = findViewById(R.id.btn_book_detail_select);
        ImageView detailback = findViewById(R.id.detail_back);
        final ImageView collect = findViewById(R.id.book_detail_collect);
        final TextView bookname = findViewById(R.id.book_detail_bookName);
        final TextView author = findViewById(R.id.book_detail_author);
        TextView booknumber = findViewById(R.id.book_detail_bookNum);
        TextView publish = findViewById(R.id.book_detail_publish);
        TextView price = findViewById(R.id.book_detail_price);
        TextView detailtype = findViewById(R.id.book_detail_type);
        TextView congbianxiang = findViewById(R.id.book_detail_congbianxiang);
        TextView topic = findViewById(R.id.book_detail_topic);

        detailback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                switch (flag){
                    case "FragmentTab1":
                        intent.setClass(BookDetailActivity.this,CollectionBookActivity.class);
                        break;
                    case "SearchAuthorActivity":
                        intent.putExtra("authorStr",authorStr);
                        intent.setClass(BookDetailActivity.this,SearchAuthorActivity.class);
                        break;
                    case "HotBookActivity":
                        intent.setClass(BookDetailActivity.this,HotBookActivity.class);
                        break;
                    case "ReadBookActivity":
                        intent.setClass(BookDetailActivity.this,ReadBookActivity.class);
                        break;
                    case "MyFavoritesActivity":
                        intent.setClass(BookDetailActivity.this,MyFavoritesActivity.class);
                        break;
                    case "RecommendBookActivity":
                        intent.putExtra("typeName",typeName);
                        intent.setClass(BookDetailActivity.this,RecommendBookActivity.class);
                        break;
                    case "SearchBooknameBooklistActivity":
                        intent.putExtra("bookListStr",bookListStr);
                        intent.setClass(BookDetailActivity.this,SearchBooknameBooklistActivity.class);
                        break;
                    case "SearchLikeAuthorActivity":
                        intent.putExtra("author",likeAuthor);
                        intent.setClass(BookDetailActivity.this,SearchLikeAuthorActivity.class);
                        break;
                    default:
                        intent.setClass(BookDetailActivity.this,CollectionBookActivity.class);
                }

                startActivity(intent);
            }
        });

        btnReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("indexId",map.get("indexId").toString());
                intent.putExtra("msg",msg);
                switch (flag){
                    case "HotBookActivity":
                        intent.putExtra("flag","HotBookActivity");
                        break;
                    case "ReadBookActivity":
                        intent.putExtra("flag","ReadBookActivity");
                        break;
                    case "MyFavoritesActivity":
                        intent.putExtra("flag","MyFavoritesActivity");
                        break;
                    case "RecommendBookActivity":
                        intent.putExtra("typeName",typeName);
                        intent.putExtra("flag","RecommendBookActivity");
                        break;
                    case "SearchBooknameBooklistActivity":
                        intent.putExtra("bookListStr",bookListStr);
                        intent.putExtra("flag","SearchBooknameBooklistActivity");
                        break;
                    case "SearchLikeAuthorActivity":
                        intent.putExtra("author",likeAuthor);
                        intent.putExtra("flag","SearchLikeAuthorActivity");
                        break;
                    case "SearchAuthorActivity":
                        intent.putExtra("authorStr",authorStr);
                        intent.putExtra("flag","SearchAuthorActivity");
                        break;
                }
                intent.setClass(BookDetailActivity.this,BookReviewActivity.class);
                startActivity(intent);
            }
        });

        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BookDetailActivity.this,HoldingInformationActivity.class);
                intent.putExtra("indexId",map.get("indexId").toString());
                intent.putExtra("flag",flag);
                intent.putExtra("msg",msg);
                if (typeName != null){
                    intent.putExtra("typeName",typeName);
                }
                if (bookListStr != null){
                    intent.putExtra("bookListStr",bookListStr);
                }
                if (author != null){
                    intent.putExtra("author",likeAuthor);
                }
                if (authorStr != null){
                    intent.putExtra("authorStr",authorStr);
                }
                startActivity(intent);
            }
        });

        bookname.setText(map.get("bookName").toString());
        author.setText(map.get("author").toString());
        booknumber.setText(map.get("indexId").toString());
        publish.setText(map.get("publisher").toString());
        price.setText(map.get("ISBN").toString() + "/" + map.get("price").toString());
        detailtype.setText(map.get("shape").toString());
        congbianxiang.setText(map.get("series").toString());
        topic.setText(map.get("typename").toString());

        RequestBody requestBody = new FormBody.Builder()
                .add("userId",Constant.user.getUserId())
                .add("indexId",map.get("indexId").toString())
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Constant.BASE_URL + "getBookMark.do")
                .build();
        final OkHttpClient okHttpClient = new OkHttpClient();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String flag = response.body().string();
                if (flag.equals("yes")){
                    flag1 = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Glide.with(BookDetailActivity.this)
                                    .load(R.drawable.collected)
                                    .into(collect);
                        }
                    });
                } else {
                    flag1 = true;
                }
            }
        });
        collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag1) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("userId",Constant.user.getUserId())
                                    .add("indexId",map.get("indexId").toString())
                                    .build();
                            Request request = new Request.Builder()
                                    .post(requestBody)
                                    .url(Constant.BASE_URL + "insetFavoriteBook.do")
                                    .build();
                            Call call = okHttpClient.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String flag = response.body().string();
                                    if (flag.equals("success")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                collect.setImageResource(R.drawable.collected);
                                                Toast.makeText(BookDetailActivity.this,
                                                        "收藏成功",Toast.LENGTH_SHORT).show();
                                                flag1 = false;
                                            }
                                        });
                                    }else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(BookDetailActivity.this,
                                                        "收藏失败",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                }
                else{
                    collect.setImageResource(R.drawable.collect);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            collect.setImageResource(R.drawable.collected);
                            RequestBody requestBody = new FormBody.Builder()
                                    .add("userId",Constant.user.getUserId())
                                    .add("indexId",map.get("indexId").toString())
                                    .build();
                            Request request = new Request.Builder()
                                    .post(requestBody)
                                    .url(Constant.BASE_URL + "deleteFavoriteBook.do")
                                    .build();
                            Call call = okHttpClient.newCall(request);
                            call.enqueue(new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                }
                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String flag = response.body().string();
                                    if (flag.equals("success")){
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                collect.setImageResource(R.drawable.collect);
                                                Toast.makeText(BookDetailActivity.this,
                                                        "取消收藏",Toast.LENGTH_SHORT).show();
                                                flag1 = true;
                                            }
                                        });
                                    }else {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(BookDetailActivity.this,
                                                        "取消收藏失败",Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
    }
}
