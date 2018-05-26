package com.example.palmarlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by as on 2018/5/17.
 */

public class choseprovinceActivity extends Activity {



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chose_province_layout);


        ImageView provinceback = findViewById(R.id.province_back);
        final ListView provincelist = findViewById(R.id.province_list);

        provinceback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(choseprovinceActivity.this,welcomeActivity.class);
                startActivity(intent);
            }
        });


        OkHttpClient okHttpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(Constant.BASE_URL + "getProvince.do")
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String provinceListStr = response.body().string();//获取从服务器端获取的字符串
                Log.e("test",provinceListStr);
                Gson gson = new Gson();
                Type type = new TypeToken<List<String>>(){}.getType();
                List<String> provincesList = gson.fromJson(provinceListStr,type);
                final choseprovinceListAdapter provinceListAdapter = new choseprovinceListAdapter(
                        choseprovinceActivity.this,provincesList,R.layout.chose_province_item_layout);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        provincelist.setAdapter(provinceListAdapter);
                    }
                });
            }
        });


    }
    public class choseprovinceListAdapter extends BaseAdapter {
        private Context context;
        private List<String> provinces;
        private int item_layout_id;
        public choseprovinceListAdapter(Context context,List<String> provinces,int item_layout_id){
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
            final TextView tvProvince = convertView.findViewById(R.id.pro_name);

            tvProvince.setText(provinces.get(position));

            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setClass(choseprovinceActivity.this,choseschoolActivity.class).putExtra("provincename",tvProvince.getText());
                    startActivity(intent);
                }
            });

            return convertView;
        }
    }
}
