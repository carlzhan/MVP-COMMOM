package com.zhan.carl.italker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zhan.carl.common.Common;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 分支提交测试
        new Common();
    }
}
