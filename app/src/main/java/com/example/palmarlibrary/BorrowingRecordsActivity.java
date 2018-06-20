package com.example.palmarlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import okhttp3.internal.tls.OkHostnameVerifier;

/**
 * Created by ruiwang on 2018/5/16.
 */

public class BorrowingRecordsActivity extends Activity {

    private int mHiddenHight = 620;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrowing_records);

        final ImageView imageView = findViewById(R.id.borrow_back);
        final ListView listView = findViewById(R.id.lv_borrow_books);
        final Context context = this.getApplicationContext();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BorrowingRecordsActivity.this,HomePageActivity.class);
                startActivity(intent);
            }
        });

        User user = Constant.user;
        OkHttpClient okHttpClient = new OkHttpClient();
        String userId = user.getUserId();
        RequestBody requestBody = new FormBody.Builder()
                .add("userId",userId)
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Constant.BASE_URL + "getBorrowRecords.do")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String str = response.body().string();
                Log.e("boolList",str);
                Gson gson = new Gson();
                Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
                List<Map<String,Object>> list = gson.fromJson(str,type);
                final BorrowRecordAdapter borrowRecordAdapter = new BorrowRecordAdapter(context,
                        R.layout.borrowing_records_book,list);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(borrowRecordAdapter);
                    }
                });

            }
        });

    }

    public class BorrowRecordAdapter extends BaseAdapter {
        private Context context;
        private int item_layout_id;
        private List<Map<String,Object>> dataSource;

        public BorrowRecordAdapter (Context context,int item_layout_id,List<Map<String,Object>> dataSource){
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                convertView = LayoutInflater.from(context).inflate(item_layout_id,null);
            }
            //实现点击展开
            final LinearLayout layoutMsg = convertView.findViewById(R.id.layout_hideArea);
            final ImageView imageView = convertView.findViewById(R.id.iv_img);
            layoutMsg.setVisibility(View.GONE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layoutMsg.getVisibility() == View.GONE){
                        //需要显示
                        layoutMsg.setVisibility(View.VISIBLE);
                        ValueAnimator animator = createDropAnimator(0,mHiddenHight,layoutMsg);
                        animator.start();
                        imageView.setImageResource(R.drawable.caretup);
                    }else{
                        //需要隐藏
                        ValueAnimator animator = createDropAnimator(mHiddenHight,0,layoutMsg);
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                layoutMsg.setVisibility(View.GONE);
                            }
                        });
                        animator.start();
                        imageView.setImageResource(R.drawable.caretdown);
                    }
                }
            });

            TextView bookName = convertView.findViewById(R.id.tv_borrow_bookname);
            TextView tiaoxingma = convertView.findViewById(R.id.tv_tiaoxingma_number);
            TextView author = convertView.findViewById(R.id.tv_author_name);
            TextView borrowTime = convertView.findViewById(R.id.tv_borrow_time_number);
            final TextView returnTime = convertView.findViewById(R.id.tv_return_time_number);
            final TextView borrowNumber = convertView.findViewById(R.id.tv_borrowagin_number);

            Button borrowAgain = convertView.findViewById(R.id.bt_borrowagin);
            borrowAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Double dou = Double.parseDouble(borrowNumber.getText().toString());
                    int number = dou.intValue();
                    if (number < 3) {
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("userId", Constant.user.getUserId())
                                .add("bookId", dataSource.get(position).get("bookId").toString())
                                .add("number",borrowNumber.getText().toString())
                                .add("returnTime",returnTime.getText().toString())
                                .build();
                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(Constant.BASE_URL + "addBorrowNumber.do")
                                .build();
                        Call call = okHttpClient.newCall(request);
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String msg = response.body().string();
                                Gson gson = new Gson();
                                Type type = new TypeToken<Map<String, Object>>() {}.getType();
                                final Map<String, Object> map = gson.fromJson(msg, type);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        returnTime.setText(map.get("returnDate").toString());
                                        borrowNumber.setText(map.get("number").toString());
                                        Toast.makeText(BorrowingRecordsActivity.this,
                                                "续借成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(BorrowingRecordsActivity.this,
                                        "续借次数不能查过三次", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }
            });


            bookName.setText(dataSource.get(position).get("bookName").toString());
            tiaoxingma.setText(dataSource.get(position).get("indexId").toString());
            author.setText(dataSource.get(position).get("author").toString());
            borrowTime.setText(dataSource.get(position).get("borrowDate").toString());
            returnTime.setText(dataSource.get(position).get("returnDate").toString());
            borrowNumber.setText(dataSource.get(position).get("number").toString());
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
