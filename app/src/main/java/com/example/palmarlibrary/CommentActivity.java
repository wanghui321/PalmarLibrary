package com.example.palmarlibrary;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 52943 on 2018/5/22.
 */

public class CommentActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comment_layout);
        ImageView back = findViewById(R.id.comment_back);
        TextView comment = findViewById(R.id.my_comment);
        Button submit = findViewById(R.id.confirm);

    }
}
