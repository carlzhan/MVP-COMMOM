package com.zhan.carl.italker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.zhan.carl.common.Common;
import com.zhan.carl.common.app.Activity;

import butterknife.BindView;


public class MainActivity extends Activity {

    @BindView(R.id.mTvTest)
    TextView mTvTest;
    
    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mTvTest.setText("测试");
    }
}
