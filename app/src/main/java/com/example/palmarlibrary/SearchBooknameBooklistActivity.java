package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
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

import java.io.IOException;
import java.lang.reflect.Type;
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

/**
 * Created by ruiwang on 2018/6/6.
 */

public class SearchBooknameBooklistActivity extends Activity {

    private List<Map<String,Object>> bookList = new ArrayList<>();
    private String bookListStr = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_search_booklist_layout);
        ImageView imageView = findViewById(R.id.bookname_search_book_back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SearchBooknameBooklistActivity.this,CollectionBookActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        bookListStr = intent.getStringExtra("bookListStr");

        Gson gson = new Gson();
        Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
        bookList = gson.fromJson(bookListStr,type);
        BookNameAdapter adapter = new BookNameAdapter(SearchBooknameBooklistActivity.this,R.layout.collection_search_booklist_item_layout, bookList);
        ListView listView = findViewById(R.id.bookname_search_book_list);
        listView.setAdapter(adapter);
    }

    public class BookNameAdapter extends BaseAdapter {
        private Context context;
        private int item_layout_id;
        private List<Map<String,Object>> dataSource;

        public BookNameAdapter (Context context,int item_layout_id,List<Map<String,Object>> dataSource){
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

            final TextView bookName = convertView.findViewById(R.id.bookname_search_bookName);
            final TextView author = convertView.findViewById(R.id.bookname_search_author);
            final ImageView imageView = convertView.findViewById(R.id.bookname_search_bookImg);
            final TextView searchnumber = convertView.findViewById(R.id.bookname_search_hot);

            bookName.setText(dataSource.get(position).get("bookName").toString());
            author.setText(dataSource.get(position).get("author").toString());
            searchnumber.setText(dataSource.get(position).get("hot").toString()
                    .substring(0,dataSource.get(position).get("hot").toString().indexOf(".")));
            String myUrl = dataSource.get(position).get("imgUrl").toString();
            Glide.with(SearchBooknameBooklistActivity.this)
                    .load(Constant.BASE_URL + myUrl)
                    .into(imageView);

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
                            intent.putExtra("flag","SearchBooknameBooklistActivity");
                            intent.putExtra("bookListStr",bookListStr);
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
