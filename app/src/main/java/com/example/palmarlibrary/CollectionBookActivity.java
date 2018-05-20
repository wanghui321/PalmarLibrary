package com.example.palmarlibrary;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by ruiwang on 2018/5/20.
 */

public class CollectionBookActivity extends FragmentActivity {

    //定义一个布局
    private LayoutInflater layoutInflater;

    private String mFragmentTags[] = {"书名", "作者", "类型"};
    //定义FragmentTabHost对象
    private FragmentTabHost myTabhost;

    // 加载Fragment页面
    private Class mFragment[] = {
            CollectionBookFragmentOne.class,
            CollectionBookFragmentTwo.class,
            CollectionBookFragmentThree.class
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_inquiries_layout);
        initTabHost(); // 初始化FragmentTabHost，并创建选项卡

        // 在Activity的onCreate方法中给FragmentTabHost控件注册选项改变事件监听器
        // 注册事件监听器
        myTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                Toast.makeText(getApplicationContext(), tabId, Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initTabHost() {
        myTabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        myTabhost.setup(this, getSupportFragmentManager(), android.R.id.tabhost);
        // 去掉分割线
        myTabhost.getTabWidget().setDividerDrawable(null);
        for (int i = 0; i < mFragmentTags.length; i++) {
            // 对Tab按钮添加标记和图片，getTextView(i)方法是获取了一个标签项
            TabHost.TabSpec tabSpec = myTabhost
                    .newTabSpec(mFragmentTags[i]).setIndicator(getTextView(i));
            // 添加Fragment
            myTabhost.addTab(tabSpec, mFragment[i], null);
        }
// 设置默认选中的选项卡
        myTabhost.setCurrentTab(0);
    }

    private View getTextView(int index){
        View view = getLayoutInflater().inflate(R.layout.collection_fragment_tab, null);
        TextView textView = view.findViewById(R.id.txt_tab);
        textView.setText(mFragmentTags[index]);
        return view;
    }




}