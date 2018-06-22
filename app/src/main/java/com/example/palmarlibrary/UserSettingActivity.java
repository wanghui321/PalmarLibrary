package com.example.palmarlibrary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 52943 on 2018/5/22.
 */

public class UserSettingActivity extends Activity{

    private static final int REQUEST_PERMISSION = 1;
    private static final int REQUEST_CODE = 2;
    ImageView imgHead = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting_layout);
        imgHead = findViewById(R.id.user_setting_user_head);
        final TextView userName = findViewById(R.id.user_setting_username);
        final TextView cardNum = findViewById(R.id.user_setting_card_number);
        final EditText realName = findViewById(R.id.user_setting_user_realname);
        final EditText departName = findViewById(R.id.user_setting_department_name);
        final EditText userMail = findViewById(R.id.user_setting_user_mail);
        final ImageView exLogin = findViewById(R.id.user_setting_exchange_login);
        final Button userSetting = findViewById(R.id.user_setting_btn);
        ImageView back = findViewById(R.id.user_setting_back);

        SharedPreferences preferences1 = getSharedPreferences("userData",Context.MODE_PRIVATE);
        final String schoolName = preferences1.getString("schoolName",null);

        //切换账户
        exLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(UserSettingActivity.this,ExchangeLoginActivity.class);
                SharedPreferences preferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.putString("schoolName",schoolName);
                editor.commit();
                startActivity(intent);
            }
        });

        //修改头像
        imgHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityCompat.requestPermissions(UserSettingActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_PERMISSION);
            }
        });


        //修改信息提交
        userSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = Constant.user;
                user.setNickname(userName.getText().toString());
                user.setUserName(realName.getText().toString());
                user.setDepartment(departName.getText().toString());
                user.setEmail(userMail.getText().toString());
                RequestBody requestBody = new FormBody.Builder()
                        .add("userId",user.getUserId())
                        .add("nickname",userName.getText().toString())
                        .add("userName",realName.getText().toString())
                        .add("department",departName.getText().toString())
                        .add("email",userMail.getText().toString())
                        .build();
                Request request = new Request.Builder()
                        .post(requestBody)
                        .url(Constant.BASE_URL + "usersetting.do")
                        .build();
                OkHttpClient okHttpClient = new OkHttpClient();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String msg = response.body().string();
                        if (msg.equals("success")){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toastTip = Toast.makeText(UserSettingActivity.this,"修改成功",Toast.LENGTH_SHORT);
                                    toastTip.setGravity(Gravity.CENTER,0,0);
                                    toastTip.show();
                                }
                            });

                        } else{
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toastTip = Toast.makeText(UserSettingActivity.this,"修改失败",Toast.LENGTH_SHORT);
                                    toastTip.setGravity(Gravity.CENTER,0,0);
                                    toastTip.show();
                                }
                            });
                        }
                    }
                });
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(UserSettingActivity.this,HomePageActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences preferences = getSharedPreferences("userData",Context.MODE_PRIVATE);
        User user = Constant.user;
        userName.setText(user.getNickname());
        cardNum.setText(preferences.getString("userId",null));
        if (preferences.getString("userName",null) != null){
            realName.setText(preferences.getString("userName",null));
        }
        if (preferences.getString("department",null) != null){
            departName.setText(preferences.getString("department",null));
        }
        if (preferences.getString("email",null) != null){
            userMail.setText(preferences.getString("email",null));
        }
        if (preferences.getString("imgUrl",null) != null &&
                !preferences.getString("imgUrl",null).equals("")){
            RequestOptions requestOptions = new RequestOptions().circleCrop();
            Glide.with(UserSettingActivity.this)
                    .load(Constant.BASE_URL + preferences.getString("imgUrl",null))
                    .apply(requestOptions)
                    .into(imgHead);
        } else {
            RequestOptions requestOptions = new RequestOptions().circleCrop();
            Glide.with(UserSettingActivity.this)
                    .load(R.drawable.userhead)
                    .apply(requestOptions)
                    .into(imgHead);
        }
    }

    //相册界面返回后的回调方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            //获取照片
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri,null,null,
                    null,null);
            cursor.moveToFirst();
            String column = MediaStore.Images.Media.DATA;
            int columnIndex = cursor.getColumnIndex(column);
            String path = cursor.getString(columnIndex);
            File file = new File(path);
            doUploadFile(file);
        }
    }

    private void doUploadFile(File file) {
        Log.e("xiangce","123123123");
        //构建请求体
        SimpleDateFormat df = new SimpleDateFormat("_yyyy_MM_dd_HH_mm_ss");
        String time = df.format(new Date());
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("image/#"),file);
        MultipartBody multipartBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("userId",Constant.user.getUserId())
                .addFormDataPart("mPhoto",Constant.user.getUserId()+time +".jpg",requestBody)
                .build();
        Request request = new Request.Builder()
                .url(Constant.BASE_URL + "uploadFile.do")
                .post(multipartBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        Call call =okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String realPath = response.body().string();
                final User user = Constant.user;
                user.setImgUrl(realPath);
                final RequestOptions requestOptions = new RequestOptions().circleCrop();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences preferences = getSharedPreferences("userData",Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("imgUrl",user.getImgUrl());
                        editor.commit();
                        Glide.with(UserSettingActivity.this)
                                .load(Constant.BASE_URL + user.getImgUrl())
                                .apply(requestOptions)
                                .into(imgHead);
                    }
                });
            }
        });
    }

    //申请动态权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //打开手机相册
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/#");
        startActivityForResult(intent,REQUEST_CODE);
    }
}
