package com.example.palmarlibrary;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

/**
 * Created by as on 2018/5/16.
 */

public class welcomeActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_layout);
        TextView view = findViewById(R.id.anima);
        Animator animator = AnimatorInflater.loadAnimator(this, R.animator.set_animation); // 载入XML动画
        animator.setTarget(view); // 设置动画对象
        animator.setDuration(5000);//shichang
        animator.setStartDelay(500);
        animator.start();
    }
}
