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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/5/21.
 */

public class FragmentTab3 extends Fragment {

    //从数据库读取的图书类型列表
    private List<String> bookTypeList = new ArrayList<>();
    //用户选择搜索的图书类型列表
    private List<String> selectTypeList = new ArrayList<>();
    private Handler handler = null;
    private ListView listView = null;
    private TypeNameAdapter adapter = null;
    private Map<Integer, Boolean> status = new HashMap<Integer, Boolean>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.collection_type_layout, container, false);

        final Context context = this.getActivity();
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
                String bookTypeListStr = response.body().string();
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
                                selectTypeList.add(entry.getKey().toString());
                            }
                        }

                        Intent intent = new Intent(context,CollectionBookByTypeActivity.class);

                        Gson gson = new Gson();
                        Type type = new TypeToken<List<String>>(){}.getType();
                        String bookTypeListStr = gson.toJson(selectTypeList,type);
                        intent.putExtra("selectTypeList",bookTypeListStr);

                        startActivity(intent);


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

            cb_type.setChecked(status.get(position));
            tv_typename.setText(dataSource.get(position).toString());
            cb_type.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    status.put(position,!status.get(position));
                }
            });

//            Button searchType = convertView.findViewById(R.id.select_by_type);
//            searchType.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    for(Map.Entry<Integer,Boolean> entry : status.entrySet()){
//                        if(entry.getValue()){
//                            selectTypeList.add(entry.getKey().toString());
//                        }
//                    }
//
//
//
//
//                }
//            });

            return convertView;

//            final TextView tv_typeName = convertView.findViewById(R.id.tv_typeName);
//
//            final CheckBox cb_type = convertView.findViewById(R.id.type_cb);
//
//            final Button search = convertView.findViewById(R.id.select_by_type);
//            tv_typeName.setText(dataSource.get(position).toString());



        }
    }













//    public class MultiSelectOrderAdapter extends RecyclerView.Adapter<MultiSelectOrderAdapter.MultiSelectOrderViewHolder> {
//
//        private Context mContext;
//
//        private List<String> mList = new ArrayList<>();
//
//        private List<String> mSelectList = new ArrayList<>();
//
//        private Map<Integer, Boolean> mMap = new HashMap<>();
//
//        public MultiSelectOrderAdapter(Context context) {
//            mContext = context;
//        }
//
//        public void setDataList(List<String> list) {
//            mList = list;
//
//            initMap();
//
//            notifyDataSetChanged();
//        }
//
//        // 初始化集合，默认不选中
//        private void initMap() {
//            for (int i = 0; i < mList.size(); i++) {
//                mMap.put(i, false);
//            }
//        }
//
//        // 返回给 Activity 当前 CheckBox 选择的顺序的数据
//        public List<String> getSelectList() {
//            return mSelectList;
//        }
//
//        @Override
//        public MultiSelectOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(mContext).inflate(R.layout.item_multi_select_order, parent, false);
//            return new MultiSelectOrderViewHolder(view);
//        }
//
//        @Override
//        public void onBindViewHolder(MultiSelectOrderViewHolder holder, final int position) {
//            holder.tvName.setText(mList.get(position));
//
//            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                    mMap.put(position, isChecked);
//
//                /* 将 CheckBox 选中的 item 按选择顺序添加到 List 里 */
//                    if (isChecked) {
//                        // 将 position 对应的 item 存到 List 里
//                        mSelectList.add(mList.get(position));
//                    } else {
//                        // 将 position 对应的 item 从 List 里移除
//                        mSelectList.remove(mList.get(position));
//                    }
//                }
//            });
//
//            if (mMap.get(position) == null) {
//                mMap.put(position, false);
//            }
//
//            // 根据 Map 来设置 CheckBox 的选中状况
//            holder.checkBox.setChecked(mMap.get(position));
//        }
//
//        @Override
//        public int getItemCount() {
//            return mList == null ? 0 : mList.size();
//        }
//
//        public class MultiSelectOrderViewHolder extends RecyclerView.ViewHolder {
//
//            CheckBox checkBox;
//            TextView tvName;
//
//            public MultiSelectOrderViewHolder(View itemView) {
//                super(itemView);
//                checkBox = itemView.findViewById(R.id.cb_multi);
//                tvName = itemView.findViewById(R.id.tv_name_multi);
//            }
//        }
//    }


}





