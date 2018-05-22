package com.example.palmarlibrary;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ruiwang on 2018/5/20.
 */

public class CollectionBookActivity extends AppCompatActivity {

    private FragmentTabHost myTabHost;
    private Map<String,View> map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection_inquiries_layout);

        map = new HashMap<>();

        //初始化
        initTabHost();
    }

    private void initTabHost() {
        myTabHost = findViewById(android.R.id.tabhost);
        myTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabhost);

        addTabSpec("tab1","书名",FragmentTab1.class);
        addTabSpec("tab2","作者",FragmentTab2.class);
        addTabSpec("tab3","类型",FragmentTab3.class);

        myTabHost.setCurrentTab(0);
    }
    private void addTabSpec(String id,String text,Class<?> fragment){
        View viewTab = getTabView(text);
        TabHost.TabSpec tabSpec = myTabHost.newTabSpec(id)
                .setIndicator(viewTab);
        myTabHost.addTab(tabSpec, fragment, null);
        map.put(id,viewTab);
    }

    private View getTabView(String str) {
        View view = getLayoutInflater().inflate(R.layout.fragment_tab_layout,null);
        TextView textView = view.findViewById(R.id.tv_tab);
        textView.setText(str);
        return view;
    }

}