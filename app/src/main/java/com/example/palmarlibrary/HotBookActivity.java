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
 * Created by 52943 on 2018/5/22.
 */

public class HotBookActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hot_book_layout);
        ImageView back = findViewById(R.id.hot_book_back);
        TextView recommend = findViewById(R.id.hot_book_recommend);
        final ListView listView = findViewById(R.id.hot_book_lv);

        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotBookActivity.this,PersonalActivity.class);
                startActivity(intent);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HotBookActivity.this,HomePageActivity.class);
                startActivity(intent);
            }
        });

        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.BASE_URL + "getHotBook.do")
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
                Gson gson = new Gson();
                Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
                List<Map<String,Object>> bookList = gson.fromJson(bookListStr,type);
                final HotBookAdapter adapter = new HotBookAdapter(HotBookActivity.this,
                        R.layout.hot_book_item_layout,bookList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(adapter);
                    }
                });
            }
        });
    }

    public class HotBookAdapter extends BaseAdapter {

        private Context context;
        private int item_layout_id;
        private List<Map<String,Object>> dataSource;

        public HotBookAdapter (Context context,int item_layout_id,List<Map<String,Object>> dataSource){
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

            ImageView imageView = convertView.findViewById(R.id.hot_book_bookImg);
            final TextView bookName = convertView.findViewById(R.id.hot_book_bookName);
            final TextView author = convertView.findViewById(R.id.hot_book_author);
            TextView hot = convertView.findViewById(R.id.hot_book_hot);

            Glide.with(HotBookActivity.this)
                    .load(Constant.BASE_URL + dataSource.get(position).get("imgUrl"))
                    .into(imageView);
            bookName.setText(dataSource.get(position).get("bookName").toString());
            author.setText(dataSource.get(position).get("author").toString());
            hot.setText(dataSource.get(position).get("hot").toString());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("bookName",bookName.getText().toString())
                            .add("author",author.getText().toString())
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
                            intent.putExtra("flag","HotBookActivity");
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
