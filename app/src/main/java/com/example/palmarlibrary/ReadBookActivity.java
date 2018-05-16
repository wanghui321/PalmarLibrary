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
import java.util.List;

/**
 * Created by Administrator on 2018/5/15.
 */

public class ReadBookActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read_book_layout);

        List<Book> books = new ArrayList<Book>();
        for (int i = 0; i < 10; ++i){
            Book book = new Book();
            book.setBookName("钢铁是怎样炼成的" + i);
            book.setAuthor("奥斯特洛夫斯基" + i);
            books.add(book);
        }
        ListView listView = findViewById(R.id.read_book_list);
        ReadBookListAdapter readBookListAdapter = new ReadBookListAdapter(this,
                books,R.layout.read_book_item_layout);
        listView.setAdapter(readBookListAdapter);
    }

    public class ReadBookListAdapter extends BaseAdapter{
        private Context context;
        private List<Book> books;
        private int item_layout_id;
        public ReadBookListAdapter(Context context,List<Book> books,int item_layout_id){
            this.context = context;
            this.books = books;
            this.item_layout_id = item_layout_id;
        }

        @Override
        public int getCount() {
            return books.size();
        }

        @Override
        public Object getItem(int position) {
            return books.get(position);
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

            TextView tvRedBookName = convertView.findViewById(R.id.red_bookName);
            TextView tvRedAuthor = convertView.findViewById(R.id.red_author);

            Book book = books.get(position);
            tvRedBookName.setText(book.getBookName());
            tvRedAuthor.setText(book.getAuthor());
            return convertView;
        }
    }
}
