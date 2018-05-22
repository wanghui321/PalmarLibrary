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

import java.util.List;

/**
 * Created by as on 2018/5/17.
 */

public class choseschoolActivity extends Activity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_school_layout);

        ImageView schoolback = findViewById(R.id.school_back);
        ListView schoollist = findViewById(R.id.school_list);

        schoollist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //intent.setClass(choseschoolActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        schoolback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(choseschoolActivity.this,choseprovinceActivity.class);
                startActivity(intent);
            }
        });

    }
    public class choseschoolListAdapter extends BaseAdapter {
        private Context context;
        private List<School> schools;
        private int item_layout_id;
        public choseschoolListAdapter(Context context,List<School> schools,int item_layout_id){
            this.context = context;
            this.schools = schools;
            this.item_layout_id = item_layout_id;
        }

        @Override
        public int getCount() {
            return schools.size();
        }

        @Override
        public Object getItem(int position) {
            return schools.get(position);
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
            ImageView tvProImg = convertView.findViewById(R.id.pro_img);
            TextView tvRedBookName = convertView.findViewById(R.id.pro_name);
            TextView tvRedAuthor = convertView.findViewById(R.id.pro_name);

            School school = schools.get(position);
            tvRedBookName.setText(school.getName());
            tvRedAuthor.setText(school.getName());
            return convertView;
        }
    }
}
