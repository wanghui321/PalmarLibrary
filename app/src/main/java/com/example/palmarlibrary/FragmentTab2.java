package com.example.palmarlibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
 * Created by Administrator on 2018/5/21.
 */

public class FragmentTab2 extends android.support.v4.app.Fragment {

    private Handler handler=null;
    private ListView listView=null;
    private AuthorNameAdapter adapter=null;
    private Context context = null;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.collection_author_layout,container,false);
        final EditText searchAuthor = view.findViewById(R.id.searchauthorname);
        context = this.getActivity();
        final OkHttpClient okHttpClient = new OkHttpClient();
        Button button = view.findViewById(R.id.btn_searchauthorname);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String author = searchAuthor.getText().toString();
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("author",author)
                        .build();
                Request request = new Request.Builder()
                        .post(requestBody)
                        .url(Constant.BASE_URL + "searchLikeAuthor.do")
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
                        if(bookListStr.equals("[]")){
                            new Thread(){
                                @Override
                                public void run() {
                                    super.run();
                                    handler.post(runnable);
                                }
                            }.start();
                        }
                        Intent intent = new Intent();
                        intent.putExtra("bookListStr",bookListStr);
                        intent.putExtra("author",author);
                        intent.setClass(context,SearchLikeAuthorActivity.class);
                        startActivity(intent);
                    }
                });

            }
        });

        handler=new Handler();
        Request request = new Request.Builder()
                .url(Constant.BASE_URL+"getAuthor.do")
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
                Type type = new TypeToken<List<String>>(){}.getType();
                List<String>bookList = gson.fromJson(bookListStr,type);
                Log.e("bookList",bookList.size()+"");
                adapter = new AuthorNameAdapter(context,R.layout.author_item_layout,
                        bookList);
                listView = view.findViewById(R.id.lv_collection_author);
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

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent();
            intent.putExtra("flag","CollectionBookActivity");
            intent.setClass(context,NoBookActivity.class);
            startActivity(intent);
        }
    };

    public class AuthorNameAdapter extends BaseAdapter{
        private Context context;
        private int item_layout_id;
        private List<String>dataSource;

    public AuthorNameAdapter(Context context,int item_layout_id,List<String>dataSource){
        this.context = context;
        this.item_layout_id = item_layout_id;
        this.dataSource = dataSource;
    }
    public int getCount(){ return dataSource.size();}

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
            Log.e("author",dataSource.get(position).toString());

            TextView tv_author = convertView.findViewById(R.id.collection_author_tv);
            tv_author.setText(dataSource.get(position).toString());

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("authorStr",dataSource.get(position).toString());
                    intent.setClass(context,SearchAuthorActivity.class);
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }
}
