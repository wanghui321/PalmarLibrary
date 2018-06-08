package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
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
 * Created by Administrator on 2018/5/23.
 */

public class PersonalActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendation_layout);

        ImageView imageView = findViewById(R.id.recommend_back);
        final GridView gridView = findViewById(R.id.recommendation_book);
                imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalActivity.this,HotBookActivity.class);
                startActivity(intent);
            }
        });

        RequestBody requestBody = new FormBody.Builder()
                .add("userId",Constant.user.getUserId())
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Constant.BASE_URL + "getInterest.do")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String interestListStr = response.body().string();
                Gson gson = new Gson();
                Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
                List<Map<String,Object>> interestList = gson.fromJson(interestListStr,type);
                final PersonalAdapter personalAdapter = new PersonalAdapter(PersonalActivity.this,
                        R.layout.recommendation_type_ite_layout,interestList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gridView.setAdapter(personalAdapter);
                    }
                });
            }
        });

    }

    public class PersonalAdapter extends BaseAdapter {

        private Context context;
        private int item_layout_id;
        private List<Map<String,Object>> dataSource;

        public PersonalAdapter (Context context,int item_layout_id,List<Map<String,Object>> dataSource){
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
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(item_layout_id,null);
            }
            final TextView tvType = convertView.findViewById(R.id.recommend_type_name);
            TextView tvNumber = convertView.findViewById(R.id.recommend_type_booknum);
            tvType.setText(dataSource.get(position).get("typeName").toString());
            tvNumber.setText(dataSource.get(position).get("number").toString());
            final String type = dataSource.get(position).get("typeName").toString();
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("typeName",type);
                    intent.setClass(PersonalActivity.this,RecommendBookActivity.class);
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
}
