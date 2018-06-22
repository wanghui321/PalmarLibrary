package com.example.palmarlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.*;
import java.lang.reflect.Type;
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
 * Created by Administrator on 2018/5/17.
 */

public class HoldingInformationActivity extends Activity{

    private int mHiddenHight = 450;
    private List<Map<String,Object>> dataSource = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holding_information_layout);
        ImageView back = findViewById(R.id.holding_back_img);

        Intent intent = getIntent();
        final String indexId = intent.getStringExtra("indexId");
        final String msg = intent.getStringExtra("msg");
        final String flag = intent.getStringExtra("flag");
        final String typeName = intent.getStringExtra("typeName");
        final String bookListStr = intent.getStringExtra("bookListStr");
        final String author = intent.getStringExtra("author");
        final String authorStr = intent.getStringExtra("authorStr");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("msg",msg);
                intent.putExtra("flag",flag);
                if (typeName != null){
                    intent.putExtra("typeName",typeName);
                }
                if (bookListStr != null){
                    intent.putExtra("bookListStr",bookListStr);
                }
                if (author != null){
                    intent.putExtra("likeAuthor",author);
                }
                if (authorStr != null){
                    intent.putExtra("author",authorStr);
                }
                intent.setClass(HoldingInformationActivity.this,BookDetailActivity.class);
                startActivity(intent);
            }
        });

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("indexId",indexId)
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Constant.BASE_URL + "location.do")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bookListStr = response.body().string();
                Log.e("boolList",bookListStr);
                Gson gson = new Gson();
                Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
                dataSource = gson.fromJson(bookListStr,type);
                Log.e("bookList",dataSource.size()+"");
                final ListView holdingInformation = findViewById(R.id.holding_information_list);
                final HoldingInformationAdapter holdingInformationAdapter = new HoldingInformationAdapter(HoldingInformationActivity.this,
                        R.layout.holding_informatin_item_layout,dataSource);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        holdingInformation.setAdapter(holdingInformationAdapter);
                    }
                });
            }
        });
    }


    public class HoldingInformationAdapter extends BaseAdapter{

        private Context context;
        private int layout_item_id;
        private List<Map<String,Object>> dataSource;
        public HoldingInformationAdapter (Context context,int layout_item_id,
                                          List<Map<String,Object>> dataSource){
            this.context = context;
            this.layout_item_id = layout_item_id;
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
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(layout_item_id,null);
            }

            TextView tv_bookName = convertView.findViewById(R.id.holding_bookName);
            TextView tv_author = convertView.findViewById(R.id.holding_book_author);
            TextView tv_place = convertView.findViewById(R.id.holding_book_place);
            TextView tv_id = convertView.findViewById(R.id.holding_book_id);
            TextView tv_state = convertView.findViewById(R.id.holding_book_state);

            tv_bookName.setText(dataSource.get(position).get("bookName").toString());
            tv_author.setText(dataSource.get(position).get("author").toString());
            tv_place.setText(dataSource.get(position).get("location").toString());
            tv_id.setText(dataSource.get(position).get("indexId").toString());
            tv_state.setText(dataSource.get(position).get("status").toString());

            final LinearLayout holdingBookDetail = convertView.findViewById(R.id.holding_book_detail);
            final ImageView imageView = convertView.findViewById(R.id.holding_care_img);
            holdingBookDetail.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.caretdown);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holdingBookDetail.getVisibility() == View.GONE){
                        //需要显示
                        holdingBookDetail.setVisibility(View.VISIBLE);
                        ValueAnimator animator = createDropAnimator(0,mHiddenHight,holdingBookDetail);
                        animator.start();
                        imageView.setImageResource(R.drawable.caretup);
                    } else {
                        //需要隐藏
                        ValueAnimator animator = createDropAnimator(mHiddenHight,0,holdingBookDetail);
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                holdingBookDetail.setVisibility(View.GONE);
                            }
                        });
                        animator.start();
                        imageView.setImageResource(R.drawable.caretdown);
                    }
                }
            });
            return convertView;
        }
    }

    private ValueAnimator createDropAnimator(int start,int end,final View view){
        ValueAnimator animator = ValueAnimator.ofInt(start,end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int)animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}
