package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by as on 2018/5/17.
 */

public class choseschoolActivity extends Activity {
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_school_layout);

        final ImageView schoolback = findViewById(R.id.school_back);
        final ListView schoollist = findViewById(R.id.school_list);

        schoolback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(choseschoolActivity.this,choseprovinceActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String province = intent.getStringExtra("provincename");
        Log.e("province",province);
        Resources res = getResources();
        String [] school = null;
        switch (province){
            case "河北省":
                school = res.getStringArray(R.array.河北);
                break;
            case "北京市":
                school = res.getStringArray(R.array.北京);
                break;
        }
        ChoseschoolListAdapter adapter = new ChoseschoolListAdapter(
            choseschoolActivity.this,school,R.layout.chose_school_item_layout);
        schoollist.setAdapter(adapter);

    }
    public class ChoseschoolListAdapter extends BaseAdapter {
        private Context context;
        private String [] schools;
        private int item_layout_id;
        public ChoseschoolListAdapter(Context context,String [] schools,int item_layout_id){
            this.context = context;
            this.schools = schools;
            this.item_layout_id = item_layout_id;
        }

        @Override
        public int getCount() {
            return schools.length;
        }

        @Override
        public Object getItem(int position) {
            return schools[position];
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
            final TextView tvSchName = convertView.findViewById(R.id.sch_name);

            String schoolName = schools[position];
            tvSchName.setText(schoolName);

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(choseschoolActivity.this,LoginActivity.class).putExtra("schoolname",tvSchName.getText());
                    startActivity(intent);
                }
            });
            return convertView;
        }
    }
}
