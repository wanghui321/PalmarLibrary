package com.example.palmarlibrary;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.*;
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
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2018/5/15.
 */

public class ReadBookActivity extends Activity{
    private ReadBookListAdapter adapter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_book_layout);
        ImageView imageView = findViewById(R.id.read_book_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadBookActivity.this,HomePageActivity.class);
                startActivity(intent);
            }
        });

        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("userId",Constant.user.getUserId())
                .build();
        Request requst = new Request.Builder()
                .post(requestBody)
                .url(Constant.BASE_URL+"getReadBook.do")
                .build();
        Call call = okHttpClient.newCall(requst);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bookListStr = response.body().string();
                Log.e("boolList",bookListStr);
                Gson gson = new Gson();
                java.lang.reflect.Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
                final List<Map<String,Object>>bookList = gson.fromJson(bookListStr,type);
                Log.e("bookList",bookList.size()+"");
                final ListView listView = findViewById(R.id.read_book_list);
                final ReadBookListAdapter adapter = new ReadBookListAdapter(ReadBookActivity.this
                        ,bookList,R.layout.read_book_item_layout);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(adapter);
                    }
                });
            }
        });
    }

    public class ReadBookListAdapter extends BaseAdapter{
        private Context context;
        private List<Map<String,Object>> books;
        private int item_layout_id;
        public ReadBookListAdapter(Context context,List<Map<String,Object>> books,int item_layout_id){
            this.context = context;
            this.books = books;
            this.item_layout_id = item_layout_id;
        }

        @Override
        public int getCount() {
            return books.size();
        }

        @Override
        public Object getItem(int position) {
            return books.get(position);
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

            ImageView imageView = convertView.findViewById(R.id.red_bookImg);
            final TextView tvRedBookName = convertView.findViewById(R.id.red_bookName);
            final TextView tvRedAuthor = convertView.findViewById(R.id.red_author);
            TextView tvRedHot = convertView.findViewById(R.id.read_hot);

            tvRedBookName.setText(books.get(position).get("bookName").toString());
            tvRedAuthor.setText(books.get(position).get("author").toString());
            tvRedHot.setText(books.get(position).get("hot").toString());
            Glide.with(ReadBookActivity.this)
                    .load(Constant.BASE_URL + books.get(position).get("imgUrl").toString())
                    .into(imageView);


            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("bookName",tvRedBookName.getText().toString())
                            .add("author",tvRedAuthor.getText().toString())
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
                            intent.putExtra("flag","ReadBookActivity");
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
