package com.example.palmarlibrary;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
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
 * Created by Administrator on 2018/5/21.
 */

public class FragmentTab1 extends android.support.v4.app.Fragment {

    private List<Map<String,Object>> bookList = new ArrayList<>();
    private Handler handler=null;
    private ListView listView=null;
    private BookNameAdapter adapter=null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.collection_bookname_layout, container, false);

        final Context context = this.getActivity();
        handler=new Handler();
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
                Log.e("boolList",bookListStr);
                Gson gson = new Gson();
                Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
                bookList = gson.fromJson(bookListStr,type);
                Log.e("bookList",bookList.size()+"");
                adapter = new BookNameAdapter(context,R.layout.bookname_item_layout,
                        bookList);
                listView = view.findViewById(R.id.lv_collection_book);
                new Thread(){
                    @Override
                    public void run() {
                        handler.post(runnableUi);
                    }
                }.start();
            }
        });

        return view;
    }

    Runnable runnableUi = new Runnable() {
        @Override
        public void run() {
            listView.setAdapter(adapter);
        }
    };

    public class BookNameAdapter extends BaseAdapter{
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

            final TextView tv_bookName = convertView.findViewById(R.id.collection_bookName);
            final TextView tv_author = convertView.findViewById(R.id.collection_author);

            tv_bookName.setText(dataSource.get(position).get("bookName").toString());
            tv_author.setText(dataSource.get(position).get("author").toString());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RequestBody requestBody = new FormBody.Builder()
                            .add("bookName",tv_bookName.getText().toString())
                            .add("author",tv_author.getText().toString())
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
