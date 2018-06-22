package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
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
 * Created by Administrator on 2018/5/31 0031.
 */

public class CollectionBookByTypeActivity extends Activity {

    private ListView listView=null;
    private BookNameAdapter adapter=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_type_search_layout);

        final Intent intent = getIntent();
        String bookListStr = intent.getStringExtra("bookListStr");
        Gson gson = new Gson();
        Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
        List<Map<String,Object>> bookList = gson.fromJson(bookListStr,type);
        ImageView back = findViewById(R.id.type_search_back);
        listView = findViewById(R.id.type_search_booklist);
        adapter = new BookNameAdapter(CollectionBookByTypeActivity.this,
                R.layout.collection_type_search_item_layout,bookList);
        listView.setAdapter(adapter);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(CollectionBookByTypeActivity.this,CollectionBookActivity.class);
                startActivity(intent1);
            }
        });

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

            Log.e("bookName",dataSource.get(position).get("bookName").toString());
            Log.e("author",dataSource.get(position).get("author").toString());

            final TextView tv_bookName = convertView.findViewById(R.id.type_bookName);
            final TextView tv_author = convertView.findViewById(R.id.type_author);
            final ImageView imageView= convertView.findViewById(R.id.type_bookImg);
            final TextView tv_hot = convertView.findViewById(R.id.type_hot);

            tv_bookName.setText(dataSource.get(position).get("bookName").toString());
            tv_author.setText(dataSource.get(position).get("author").toString());
            tv_hot.setText(dataSource.get(position).get("hot").toString().substring(0,dataSource.get(position).get("hot").toString().indexOf(".")));
            Glide.with(CollectionBookByTypeActivity.this)
                    .load(Constant.BASE_URL + dataSource.get(position).get("imgUrl"))
                    .into(imageView);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("bookName",tv_bookName.getText().toString())
                            .add("author",tv_author.getText().toString())
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
                            intent.putExtra("flag","CollectionByTypeActivity");
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