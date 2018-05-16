package com.example.palmarlibrary;

import android.app.Activity;
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
 * Created by Administrator on 2018/5/16.
 */

public class BookReviewActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_review_layout);

        List<Map<String,Object>> dataSource = new ArrayList<>();
        for (int i = 0; i < 10; ++i){
            Map<String,Object> map = new HashMap<>();
            map.put("userName","张三"+i);
            map.put("detail","我很喜欢这本书" + i);
            dataSource.add(map);
        }

        BookReviewAdapter bookReviewAdapter = new BookReviewAdapter(this,
                R.layout.book_review_item_layout,dataSource);
        ListView listView = findViewById(R.id.book_review_list);
        listView.setAdapter(bookReviewAdapter);

    }

    public class BookReviewAdapter extends BaseAdapter{
        private Context context;
        private int item_layout_id;
        private List<Map<String,Object>> dataSource;

        public BookReviewAdapter (Context context,int item_layout_id,List<Map<String,Object>> dataSource){
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

            TextView bookReviewUserName = convertView.findViewById(R.id.book_review_userName);
            TextView bookReviewDetail = convertView.findViewById(R.id.book_review_detail);

            bookReviewUserName.setText(dataSource.get(position).get("userName").toString());
            bookReviewDetail.setText(dataSource.get(position).get("detail").toString());
            return convertView;
        }
    }
}
