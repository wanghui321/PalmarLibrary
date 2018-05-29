package com.example.palmarlibrary;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/5/21.
 */

public class FragmentTab2 extends android.support.v4.app.Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.collection_author_layout,container,false);

        final Context context = this.getActivity();

        OkHttpClient okHttpClient = new OkHttpClient();
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
                java.lang.reflect.Type type = new TypeToken<List<Map<String,Object>>>(){}.getType();
//                bookList = gson.fromJson(bookListStr,type);
//                Log.e("bookList",bookList.size()+"");
//                FragmentTab1.BookNameAdapter adapter = new FragmentTab1.BookNameAdapter(context,R.layout.bookname_item_layout,
//                        bookList);
                ListView listView = view.findViewById(R.id.lv_collection_book);
//                listView.setAdapter(adapter);
            }
        });

        return view;
    }
    public class AuthorNameAdapter extends BaseAdapter{
        private Context context;
        private int item_layout_id;
        private List<Map<String,Object>>dataSource;

    public AuthorNameAdapter(Context context,int item_layout_id,List<Map<String,Object>>dataSource){
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(item_layout_id,null);
            }
            Log.e("author",dataSource.get(position).get("authot").toString());
            TextView tv_author = convertView.findViewById(R.id.collection_author);
            tv_author.setText(dataSource.get(position).get("author").toString());
            return convertView;
        }
    }
}
