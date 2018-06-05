package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ruiwang on 2018/5/30.
 */

public class AddreviewActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_review_addreview);

        Intent intent = getIntent();
        final String indexId = intent.getStringExtra("indexId");
        final String msg1 = intent.getStringExtra("msg");
        final String flag = intent.getStringExtra("flag");

        ImageView addreviewback = findViewById(R.id.addreview_back);
        final EditText editText = findViewById(R.id.review_review);
        Button subreview = findViewById(R.id.addreview_submit);

        addreviewback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("indexId",indexId);
                intent.putExtra("msg",msg1);
                if (flag != null){
                    intent.putExtra("flag",flag);
                }
                intent.setClass(AddreviewActivity.this,BookReviewActivity.class);
                startActivity(intent);
            }
        });

        subreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = Constant.user;
                String str = editText.getText().toString();
                RequestBody requestBody = new FormBody.Builder()
                        .add("userId",user.getUserId())
                        .add("content",editText.getText().toString())
                        .add("indexId",indexId)
                        .build();
                Request request = new Request.Builder()
                        .post(requestBody)
                        .url(Constant.BASE_URL + "addReview.do")
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String msg = response.body().string();
                        if (msg.equals("success")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toastTip = Toast.makeText(AddreviewActivity.this,"评论添加成功",Toast.LENGTH_SHORT);
                                    toastTip.setGravity(Gravity.CENTER,0,0);
                                    toastTip.show();
                                }
                            });
                            Intent intent = new Intent();
                            intent.setClass(AddreviewActivity.this,BookReviewActivity.class);
                            intent.putExtra("indexId",indexId);
                            intent.putExtra("msg",msg1);
                            if (flag != null){
                                intent.putExtra("flag",flag);
                            }
                            startActivity(intent);
                        } else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toastTip = Toast.makeText(AddreviewActivity.this,"评论添加失败",Toast.LENGTH_SHORT);
                                    toastTip.setGravity(Gravity.CENTER,0,0);
                                    toastTip.show();
                                }
                            });
                        }
                    }
                });
            }
        });
    }
}
