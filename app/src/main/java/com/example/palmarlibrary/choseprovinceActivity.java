package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by as on 2018/5/17.
 */

public class choseprovinceActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_province_layout);



    }
    public class choseprovinceListAdapter extends BaseAdapter {
        private Context context;
        private List<Province> provinces;
        private int item_layout_id;
        public choseprovinceListAdapter(Context context,List<Province> provinces,int item_layout_id){
            this.context = context;
            this.provinces = provinces;
            this.item_layout_id = item_layout_id;
        }

        @Override
        public int getCount() {
            return provinces.size();
        }

        @Override
        public Object getItem(int position) {
            return provinces.get(position);
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

            Province province = provinces.get(position);
            tvRedBookName.setText(province.getName());
            tvRedAuthor.setText(province.getName());
            return convertView;
        }
    }
}
