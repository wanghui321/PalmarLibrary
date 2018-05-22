package com.example.palmarlibrary;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/21.
 */

public class FragmentTab1 extends android.support.v4.app.Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collection_bookname_layout,container,false);

        List<Map<String,Object>> dataSource = new ArrayList<>();
        for (int i = 0; i < 10; ++i){
            Map<String,Object> map = new HashMap<>();
            map.put("bookName","钢铁是怎样炼成的");
            map.put("author","奥斯特洛夫斯基");
            dataSource.add(map);
        }
        BookNameAdapter bookNameAdapter = new BookNameAdapter(this.getActivity(),R.layout.bookname_item_layout,dataSource);
        ListView listView = view.findViewById(R.id.lv_collection_book);
        listView.setAdapter(bookNameAdapter);
        return view;
    }

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

            TextView tv_bookName = convertView.findViewById(R.id.collection_bookName);
            TextView tv_author = convertView.findViewById(R.id.collection_author);

            tv_bookName.setText(dataSource.get(position).get("bookName").toString());
            tv_author.setText(dataSource.get(position).get("author").toString());

            return convertView;
        }
    }
}
