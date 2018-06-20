package com.example.palmarlibrary;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.VideoView;

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

public class FragmentTab3 extends Fragment {

    //从数据库读取的图书类型列表
    private List<String> bookTypeList = new ArrayList<>();
    //用户选择搜索的图书类型列表
    private ArrayList<String> selectTypeList = new ArrayList<>();
    private Handler handler = null;
    private ListView listView = null;
    private TypeNameAdapter adapter = null;
    private Map<Integer, Boolean> status = new HashMap<Integer, Boolean>();
    private Context context= null;
    private List<Map<String,Object>> bookList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.collection_type_layout, container, false);

        context = this.getActivity();
        handler = new Handler();
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url(Constant.BASE_URL + "getBookType.do")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bookTypeListStr = response.body().string();
                Log.e("bookTypeList", bookTypeListStr);


                Gson gson = new Gson();

                Type type = new TypeToken<List<String>>() {}.getType();


                bookTypeList = gson.fromJson(bookTypeListStr, type);

                Log.e("bookTypeList", bookTypeList.size() + "");

                adapter = new TypeNameAdapter(context, R.layout.collection_type_item_layout, bookTypeList);

                listView = view.findViewById(R.id.lv_collection_type);


                Button searchType = view.findViewById(R.id.select_by_type);
                searchType.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(Map.Entry<Integer,Boolean> entry : status.entrySet()){
                            if(entry.getValue()){
                                selectTypeList.add(bookTypeList.get(entry.getKey()));
                                Log.e("type:",entry.getKey().toString());
                            }
                        }
                        OkHttpClient okHttpClient = new OkHttpClient();
                        RequestBody requestBody = new FormBody.Builder()
                                .add("typeNameList",selectTypeList.toString())
                                .build();
                        Request request = new Request.Builder()
                                .post(requestBody)
                                .url(Constant.BASE_URL + "selectBookByType.do")
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
                                intent.setClass(context,CollectionBookByTypeActivity.class);
                                startActivity(intent);
                            }
                        });
                    }
                });
                new Thread() {
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
            intent.putExtra("flag","CollectionBookByType");
            intent.setClass(context,NoBookActivity.class);
            startActivity(intent);
        }
    };

    public class TypeNameAdapter extends BaseAdapter {
        private Context context;
        private int item_layout_id;
        private List<String> dataSource;


        public TypeNameAdapter(Context context, int item_layout_id, List<String> dataSource) {
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
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(item_layout_id, null);
            }

            TextView tv_typename=convertView.findViewById(R.id.tv_typeName);


            CheckBox cb_type = convertView.findViewById(R.id.type_cb);

          //  Log.e("复选框位置",status.get(position).toString());
          //  cb_type.setChecked(status.get(position));
            status.put(position,false);

            tv_typename.setText(dataSource.get(position).toString());
            cb_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    status.put(position,!status.get(position));
                }
            });
            return convertView;
        }
    }
}





