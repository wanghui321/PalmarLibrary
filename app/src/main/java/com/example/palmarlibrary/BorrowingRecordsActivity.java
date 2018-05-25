package com.example.palmarlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ruiwang on 2018/5/16.
 */

public class BorrowingRecordsActivity extends Activity {

    private int mHiddenHight = 350;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrowing_records);

        List<Map<String,Object>> dataSource = new ArrayList<>();
        for (int i = 0; i < 10; ++i){
            Map<String,Object> map = new HashMap<>();
            map.put("tiaoxingma","20111115"+i);
            map.put("author","赵老" + i);
            map.put("borrowTime","2015050"+i);
            map.put("returnTime","2016020" + i);
            map.put("borrowAgin","2"+i);
            dataSource.add(map);
        }

        BorrowRecordAdapter borrowRecordAdapter = new BorrowRecordAdapter(this,
                R.layout.borrowing_records_book,dataSource);
        ListView listView = findViewById(R.id.lv_borrow_books);
        listView.setAdapter(borrowRecordAdapter);

        ImageView borrowback = findViewById(R.id.borrow_back);
        Button borrowagin = findViewById(R.id.bt_borrowagin);

        borrowagin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //intent.setClass(BorrowingRecordsActivity.this,.class);
                startActivity(intent);
            }
        });

        borrowback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(BorrowingRecordsActivity.this,HomePageActivity.class);
                startActivity(intent);
            }
        });


    }

    public class BorrowRecordAdapter extends BaseAdapter {
        private Context context;
        private int item_layout_id;
        private List<Map<String,Object>> dataSource;

        public BorrowRecordAdapter (Context context,int item_layout_id,List<Map<String,Object>> dataSource){
            this.context = context;
            this.item_layout_id = item_layout_id;
            this.dataSource = dataSource;
        }

        @Override
        public int getCount() {
            return dataSource.size();
        }

        @Override
        public Object getItem(int position) {
            return dataSource.get(position);
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
            //实现点击展开
            final LinearLayout layoutMsg = convertView.findViewById(R.id.layout_hideArea);
            final ImageView imageView = convertView.findViewById(R.id.iv_img);
            layoutMsg.setVisibility(View.GONE);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (layoutMsg.getVisibility() == View.GONE){
                        //需要显示
                        layoutMsg.setVisibility(View.VISIBLE);
                        ValueAnimator animator = createDropAnimator(0,mHiddenHight,layoutMsg);
                        animator.start();
                        imageView.setImageResource(R.drawable.caretup);
                    }else{
                        //需要隐藏
                        ValueAnimator animator = createDropAnimator(mHiddenHight,0,layoutMsg);
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                layoutMsg.setVisibility(View.GONE);
                            }
                        });
                        animator.start();
                        imageView.setImageResource(R.drawable.caretdown);
                    }
                }
            });

            TextView tiaoxingma = convertView.findViewById(R.id.tv_tiaoxingma_number);
            TextView author = convertView.findViewById(R.id.tv_author_name);
            TextView borrowTime = convertView.findViewById(R.id.tv_borrow_time_number);
            TextView returnTime = convertView.findViewById(R.id.tv_return_time_number);
            TextView borrowAgin = convertView.findViewById(R.id.tv_borrowagin_number);

            tiaoxingma.setText(dataSource.get(position).get("tiaoxingma").toString());
            author.setText(dataSource.get(position).get("author").toString());
            borrowTime.setText(dataSource.get(position).get("borrowTime").toString());
            returnTime.setText(dataSource.get(position).get("returnTime").toString());
            borrowAgin.setText(dataSource.get(position).get("borrowAgin").toString());
            return convertView;
        }
    }

    private ValueAnimator createDropAnimator(int start,int end,final View view){
        ValueAnimator animator = ValueAnimator.ofInt(start,end);
        animator.setDuration(1000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int)animation.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                layoutParams.height = value;
                view.setLayoutParams(layoutParams);
            }
        });
        return animator;
    }
}
