package com.example.palmarlibrary;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.widget.Toast;

/**
 * Created by Administrator on 2018/5/18 0018.
 */

public class AdviseSubmitLoadingActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.advise_loading_layout);

        //这里Handler的postDelayed方法，等待10000毫秒在执行run方法。
        //在Activity中我们经常需要使用Handler方法更新UI或者执行一些耗时事件，
        //并且Handler中post方法既可以执行耗时事件也可以做一些UI更新的事情，比较好用，推荐使用
        new Handler().postDelayed(new Runnable(){
            public void run(){
                //等待10000毫秒后销毁此页面，并提示提交成功
                AdviseSubmitLoadingActivity.this.finish();
                Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT).show();
            }
        }, 10000);
    }
}
