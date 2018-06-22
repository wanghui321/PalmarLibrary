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
 * Created by Administrator on 2018/6/12.
 */

public class SearchLikeAuthorActivity extends Activity {

    private String author = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_like_author_layout);

        ImageView back = findViewById(R.id.author_like_book_back);
        final ListView listView = findViewById(R.id.auhtor_like_book_list);
        Intent intent = getIntent();
        String bookListStr = intent.getStringExtra("bookListStr");
        author = intent.getStringExtra("author");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SearchLikeAuthorActivity.this,CollectionBookActivity.class);
                startActivity(intent);
            }
        });

        Gson gson = new Gson();
        Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
        List<Map<String,Object>> bookList = gson.fromJson(bookListStr,type);
        SearchLikeAuthorAdapter adapter = new SearchLikeAuthorAdapter(SearchLikeAuthorActivity.this,
                R.layout.search_like_author_item_layout,bookList);
        listView.setAdapter(adapter);

    }

    public class SearchLikeAuthorAdapter extends BaseAdapter {

        private Context context;
        private int item_layout_id;
        private List<Map<String,Object>> dataSource;

        public SearchLikeAuthorAdapter (Context context,int item_layout_id,List<Map<String,Object>> dataSource){
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

            ImageView imageView = convertView.findViewById(R.id.author_like_bookImg);
            final TextView tvName = convertView.findViewById(R.id.author_like_bookName);
            final TextView tvAuthor = convertView.findViewById(R.id.author_like_author);
            TextView tvHot = convertView.findViewById(R.id.author_like_hot);
            Glide.with(SearchLikeAuthorActivity.this)
                    .load(Constant.BASE_URL + dataSource.get(position).get("imgUrl"))
                    .into(imageView);
            tvName.setText(dataSource.get(position).get("bookName").toString());
            tvAuthor.setText(dataSource.get(position).get("author").toString());
            tvHot.setText(dataSource.get(position).get("hot").toString().substring(0,dataSource.get(position).get("hot").toString().indexOf(".")));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("bookName",tvName.getText().toString())
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
                            intent.putExtra("likeAuthor",author);
                            intent.putExtra("flag","SearchLikeAuthorActivity");
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
