package com.zhan.carl.common.app;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by carlzhan on 2017/11/18.
 */

public abstract class Fragment extends android.support.v4.app.Fragment {

    protected View mRootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // 处理初始化参数
        initArgs(getArguments());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            int contentLayoutId = getContentLayoutId();
            // 初始化当前的根布局，并且不在view创建的时候添加到container里面 入参为false
            View view = inflater.inflate(contentLayoutId, container, false);
            initWidget(view);
        } else {
            // mRootView 被赋值，可能当fragment回收后mRootView还没被回收（在container中），则需要将mRootView移除掉
            if (mRootView.getParent() != null) {
                ((ViewGroup) mRootView.getParent()).removeView(mRootView);
            }
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 在View创建完成后初始化数据
        initData();
    }

    /**
     * 获取文件资源id
     *
     * @return 返回资源文件id
     */
    protected abstract int getContentLayoutId();

    /**
     * 初始化控件
     *
     * @param root
     */
    protected void initWidget(View root) {

    }

    /**
     * 初始化数据
     */
    protected void initData() {

    }

    /**
     * 初始化参数
     */
    protected void initArgs(Bundle bundle) {

    }

    /**
     * 返回按键触发时调用
     *
     * @return 返回true代表我已经处理返回逻辑，activity不用finish
     * 返回false代表我没有处理逻辑，activity自己走自己的逻辑
     */
    public boolean onBackPressed() {
        return false;
    }
}
