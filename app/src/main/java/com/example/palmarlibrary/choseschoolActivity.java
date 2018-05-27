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
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("province",province)
                .build();
        Request request = new Request.Builder()
                .post(requestBody)
                .url(Constant.BASE_URL + "getSchool.do")
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
                Gson gson = new Gson();
                Type type = new TypeToken<List<String>>(){}.getType();
                List<String> schoolList = gson.fromJson(provinceListStr,type);
                final choseschoolListAdapter schoolListAdapter = new choseschoolListAdapter(
                        choseschoolActivity.this,schoolList,R.layout.chose_school_item_layout);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        schoollist.setAdapter(schoolListAdapter);
                    }
                });
            }
        });

    }
    public class choseschoolListAdapter extends BaseAdapter {
        private Context context;
        private List<String> schools;
        private int item_layout_id;
        public choseschoolListAdapter(Context context,List<String> schools,int item_layout_id){
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
            final TextView tvSchName = convertView.findViewById(R.id.sch_name);

            String schoolName = schools.get(position);
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
