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
import android.widget.ListView;

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
 * Created by 52943 on 2018/5/22.
 */

public class MyFavoritesActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_favorites_layout);

        ImageView imageView = findViewById(R.id.my_favorities_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyFavoritesActivity.this,HomePageActivity.class);
                startActivity(intent);
            }
        });



        RequestBody requestBody = new FormBody.Builder()
                .add("userId",Constant.user.getUserId())
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Constant.BASE_URL + "getFavoriteBook.do")
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
                        MyFavoritesAdapter myFavoritesAdapter = new MyFavoritesAdapter(MyFavoritesActivity.this,
                                R.layout.my_favorities_item_layout,dataSource);
                        GridView gridView = findViewById(R.id.gv_book);
                        gridView.setAdapter(myFavoritesAdapter);
                    }
                });

            }
        });


    }

    public class MyFavoritesAdapter extends BaseAdapter{

        private Context context;
        private int item_layout_id;
        private List<Map<String,Object>> dataSource;

        public MyFavoritesAdapter (Context context,int item_layout_id,List<Map<String,Object>> dataSource){
            this.context = context;
            this.item_layout_id = item_layout_id;
            this.dataSource = dataSource;
        }

        @Override
        public int getCount() {
            if (dataSource.size()<=15){
                return 15;
            } else {
                return (dataSource.size()/3+1)*3;
            }
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

            ImageView imageView = convertView.findViewById(R.id.my_favorities_book);
            if (position < dataSource.size()){
                String myUrl = dataSource.get(position).get("imgUrl").toString();
                Glide.with(MyFavoritesActivity.this)
                        .load(Constant.BASE_URL + myUrl)
                        .into(imageView);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        RequestBody requestBody = new FormBody.Builder()
                                .add("bookName",dataSource.get(position).get("bookName").toString())
                                .add("author",dataSource.get(position).get("author").toString())
                                .add("userId",Constant.user.getUserId())
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
                                intent.putExtra("flag","MyFavoritesActivity");
                                intent.setClass(context,BookDetailActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
            }

            return convertView;
        }
    }
}
