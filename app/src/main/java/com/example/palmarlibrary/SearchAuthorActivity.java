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

import java.lang.reflect.Type;
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
 * Created by Administrator on 2018/5/31.
 */

public class SearchAuthorActivity extends Activity {

    String author = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_author_search_layout);
        ImageView imageView = findViewById(R.id.author_search_book_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SearchAuthorActivity.this,CollectionBookActivity.class);
                startActivity(intent);
            }
        });
        Intent intent = getIntent();
        author = intent.getStringExtra("author");
        final ListView listView = findViewById(R.id.auhtor_search_book_list);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("author",author)
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Constant.BASE_URL + "searchByAuthor.do")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String bookList = response.body().string();
                Log.e("asdasdasd",bookList);
                Gson gson = new Gson();
                Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
                List<Map<String,Object>> dataSource =  gson.fromJson(bookList,type);
                final SearchAuthorAdapter adapter = new SearchAuthorAdapter(SearchAuthorActivity.this,
                        R.layout.collection_author_search_item_layout,dataSource);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(adapter);
                    }
                });
            }
        });
    }

    public class SearchAuthorAdapter extends BaseAdapter{

        private Context context;
        private int item_layout_id;
        private List<Map<String,Object>> dataSource;

        public SearchAuthorAdapter (Context context,int item_layout_id,List<Map<String,Object>> dataSource){
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

            ImageView imageView = convertView.findViewById(R.id.author_search_bookImg);
            final TextView tvName = convertView.findViewById(R.id.author_search_bookName);
            final TextView tvAuthor = convertView.findViewById(R.id.author_search_author);
            TextView tvHot = convertView.findViewById(R.id.author_search_hot);
            Glide.with(SearchAuthorActivity.this)
                    .load(Constant.BASE_URL + dataSource.get(position).get("imgUrl"))
                    .into(imageView);
            tvName.setText(dataSource.get(position).get("bookName").toString());
            tvAuthor.setText(dataSource.get(position).get("author").toString());
            tvHot.setText(dataSource.get(position).get("hot").toString());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("bookName",tvName.getText().toString())
                            .add("author",tvAuthor.getText().toString())
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
                            intent.putExtra("author",author);
                            intent.putExtra("flag","SearchAuthorActivity");
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
