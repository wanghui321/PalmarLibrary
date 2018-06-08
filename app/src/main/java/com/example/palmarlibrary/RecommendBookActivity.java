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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Type;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/6/8.
 */

public class RecommendBookActivity extends Activity {

    private String typeName = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recommendation_book_layout);
        ImageView back = findViewById(R.id.recommend_book_back);
        final ListView tvBook = findViewById(R.id.recommend_book_booklist);

        Intent intent = getIntent();
        typeName = intent.getStringExtra("typeName");
        List<String> typeList = new ArrayList<>();
        typeList.add(typeName);
        Gson gson = new Gson();
        Type type = new TypeToken<List<String>>(){}.getType();
        String typeStr =  gson.toJson(typeList,type);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(RecommendBookActivity.this,PersonalActivity.class);
                startActivity(intent);
            }
        });

        RequestBody requestBody = new FormBody.Builder()
                .add("typeNameList",typeStr)
                .build();
        final Request request = new Request.Builder()
                .post(requestBody)
                .url(Constant.BASE_URL + "selectBookByType.do")
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
                String bookListStr = response.body().string();
                Gson gson = new Gson();
                Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
                List<Map<String,Object>> bookList = gson.fromJson(bookListStr,type);
                final RecommendBookAdapter adapter = new RecommendBookAdapter(RecommendBookActivity.this,
                        R.layout.recommendation_item_layout,bookList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvBook.setAdapter(adapter);
                    }
                });
            }
        });
    }

    private class RecommendBookAdapter extends BaseAdapter{

        private Context context;
        private int item_layout_id;
        private List<Map<String,Object>> dataSource;

        public RecommendBookAdapter (Context context,int item_layout_id,List<Map<String,Object>> dataSource){
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
            ImageView imageView = convertView.findViewById(R.id.recommend_bookImg);
            final TextView tvBookName = convertView.findViewById(R.id.recommend_bookName);
            final TextView tvAuthor = convertView.findViewById(R.id.recommend_author);
            TextView tvBookHot = convertView.findViewById(R.id.recommend_hot);
            Glide.with(RecommendBookActivity.this)
                    .load(Constant.BASE_URL + dataSource.get(position).get("imgUrl"))
                    .into(imageView);
            tvBookName.setText(dataSource.get(position).get("bookName").toString());
            tvBookHot.setText(dataSource.get(position).get("hot").toString());
            tvAuthor.setText(dataSource.get(position).get("author").toString());
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("bookName",tvBookName.getText().toString())
                            .add("author",tvAuthor.getText().toString())
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
                            intent.putExtra("flag","RecommendBookActivity");
                            intent.putExtra("typeName",typeName);
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
