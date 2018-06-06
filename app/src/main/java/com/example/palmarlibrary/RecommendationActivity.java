package com.example.palmarlibrary;

import android.app.Activity;
import android.app.DownloadManager;
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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
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
 * Created by Administrator on 2018/5/18 0018.
 */

public class RecommendationActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendation_layout);

        ImageView imageView = findViewById(R.id.read_book_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecommendationActivity.this,HomePageActivity.class);
                startActivity(intent);
            }
        });
        RequestBody requestBody = new FormBody.Builder()
                .add("userId",Constant.user.getUserId())
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Constant.BASE_URL + "getBookType.do")
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
                java.lang.reflect.Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
                final List<Map<String,Object>> dataSource = gson.fromJson(review,type);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        RecommendationActivity.RecommendationAdapter myFavoritesAdapter = new RecommendationActivity.RecommendationAdapter(RecommendationActivity.this,
                                R.layout.recommendation_type_ite_layout,dataSource);
                        GridView gridView = findViewById(R.id.recommendation_book);
                        gridView.setAdapter(myFavoritesAdapter);
                    }
                });

            }
        });

    }
    public class RecommendationAdapter extends BaseAdapter {

        private Context context;
        private int item_layout_id;
        private List<Map<String,Object>> dataSource;

        public RecommendationAdapter (Context context,int item_layout_id,List<Map<String,Object>> dataSource){
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
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(item_layout_id,null);
            }

            ImageView imageView = convertView.findViewById(R.id.recommend_bookImg);
            String myUrl = dataSource.get(position).get("imgUrl").toString();
            Glide.with(RecommendationActivity.this)
                    .load(Constant.BASE_URL + myUrl)
                    .into(imageView);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    RequestBody requestBody = new FormBody.Builder()
                            .add("bookName",dataSource.get(position).get("bookName").toString())
                            .add("hot",dataSource.get(position).get("hot").toString())
                            .build();
                    Request request = new Request.Builder()
                            .post(requestBody)
                            .url(Constant.BASE_URL + "getBookDetails.do")
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
                            Intent intent = new Intent();
                            intent.putExtra("msg",msg);
                            intent.setClass(context,BookDetailActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            });
            return convertView;
        }
    }
}
