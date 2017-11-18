package com.zhan.carl.common.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.*;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.List;

/**
 * Created by carlzhan on 2017/11/18.
 */

public abstract class Activity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWindows();

        if (initArgs(getIntent().getExtras())) {
            getContentLayoutId();
            initWidget();
            initData();
        } else {
            finish();
        }
    }

    /**
     * 初始化窗口
     */
    protected void initWindows() {

    }

    /**
     * 初始化相关参数
     *
     * @param bundle 参数Bundle
     * @return 如果参数正确返回true，错误返回false 默认返回true
     */
    protected boolean initArgs(Bundle bundle) {
        return true;
    }

    /**
     * 获取当前界面资源文件id
     *
     * @return 资源文件id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     */
    protected void initWidget() {

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * @return
     */
    @Override
    public boolean onSupportNavigateUp() {
        // 点击界面返回导航时，finish界面
        finish();
        return super.onSupportNavigateUp();
    }

    /**
     * 物理返回键
     */
    @Override
    public void onBackPressed() {
        // 遍历activity中的fragment，判断是否属于自己的fragment，判断fragment中onBackPressed是否为true，是则直接返回
        List<android.support.v4.app.Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null && fragments.size() > 0) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof com.zhan.carl.common.app.Fragment) {
                    if (((com.zhan.carl.common.app.Fragment) fragment).onBackPressed()) {
                        return;
                    }
                }
            }
        }
        super.onBackPressed();
        finish();
    }
}
