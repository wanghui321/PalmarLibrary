package com.example.palmarlibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by ruiwang on 2018/5/20.
 */

public class CollectionBookFragmentOne extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View relativeLayout = inflater.inflate(R.layout.collection_fragment_page_one, container,false);
        //listView = getView().findViewById(R.id.lv_hotbooks);
        return relativeLayout;
    }

}
