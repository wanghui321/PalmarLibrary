package com.example.palmarlibrary;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/17.
 */

public class HoldingInformationActivity extends Activity{

    private int mHiddenHight = 450;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.holding_information_layout);

        List<Map<String,Object>> dataSource = new ArrayList<>();
        for (int i = 0; i < 3; ++i){
            Map map = new HashMap();
            map.put("bookName","钢铁是怎样炼成的");
            map.put("author","奥斯特洛夫斯基");
            map.put("place","文献中心");
            map.put("id","10086");
            map.put("state","可借阅");
            dataSource.add(map);
        }
        ListView holdingInformation = findViewById(R.id.holding_information_list);
        HoldingInformationAdapter holdingInformationAdapter = new HoldingInformationAdapter(this,
                R.layout.holding_informatin_item_layout,dataSource);
        holdingInformation.setAdapter(holdingInformationAdapter);
    }

    public class HoldingInformationAdapter extends BaseAdapter{

        private Context context;
        private int layout_item_id;
        private List<Map<String,Object>> dataSource;
        public HoldingInformationAdapter (Context context,int layout_item_id,
                                          List<Map<String,Object>> dataSource){
            this.context = context;
            this.layout_item_id = layout_item_id;
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
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(layout_item_id,null);
            }

            TextView tv_bookName = convertView.findViewById(R.id.holding_bookName);
            TextView tv_author = convertView.findViewById(R.id.holding_book_author);
            TextView tv_place = convertView.findViewById(R.id.holding_book_place);
            TextView tv_id = convertView.findViewById(R.id.holding_book_id);
            TextView tv_state = convertView.findViewById(R.id.holding_book_state);

            tv_bookName.setText(dataSource.get(position).get("bookName").toString());
            tv_author.setText(dataSource.get(position).get("author").toString());
            tv_place.setText(dataSource.get(position).get("place").toString());
            tv_id.setText(dataSource.get(position).get("id").toString());
            tv_state.setText(dataSource.get(position).get("state").toString());

            final LinearLayout holdingBookDetail = convertView.findViewById(R.id.holding_book_detail);
            final ImageView imageView = convertView.findViewById(R.id.holding_care_img);
            holdingBookDetail.setVisibility(View.GONE);
            imageView.setImageResource(R.drawable.caretdown);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holdingBookDetail.getVisibility() == View.GONE){
                        //需要显示
                        holdingBookDetail.setVisibility(View.VISIBLE);
                        ValueAnimator animator = createDropAnimator(0,mHiddenHight,holdingBookDetail);
                        animator.start();
                        imageView.setImageResource(R.drawable.caretup);
                    } else {
                        //需要隐藏
                        ValueAnimator animator = createDropAnimator(mHiddenHight,0,holdingBookDetail);
                        animator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                holdingBookDetail.setVisibility(View.GONE);
                            }
                        });
                        animator.start();
                        imageView.setImageResource(R.drawable.caretdown);
                    }
                }
            });
            return convertView;
        }
    }

    private ValueAnimator createDropAnimator(int start,int end,final View view){
        ValueAnimator animator = ValueAnimator.ofInt(start,end);
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
