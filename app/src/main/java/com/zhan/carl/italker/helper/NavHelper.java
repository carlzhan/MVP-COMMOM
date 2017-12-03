package com.zhan.carl.italker.helper;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;

import com.zhan.carl.italker.R;
import com.zhan.carl.italker.frags.main.ActiveFragment;

import org.w3c.dom.ls.LSException;

/**
 * 处理fragment重用问题
 * Created by carlzhan on 2017/12/3.
 */

public class NavHelper<T> {

    private final SparseArray<Tab<T>> tabs = new SparseArray();
    private final Context context;
    private final int containerId;
    private final FragmentManager fragmentManager;
    private final OnTabChangedListener<T> listener;
    // 当前选中的Tab
    private Tab<T> currentTab;

    public NavHelper(Context context, int containerId, FragmentManager fragmentManager, OnTabChangedListener<T> listener) {
        this.context = context;
        this.containerId = containerId;
        this.fragmentManager = fragmentManager;
        this.listener = listener;
    }

    /**
     * 添加Tab
     *
     * @param menuId
     * @param tab
     */
    public NavHelper add(int menuId, Tab<T> tab) {
        tabs.put(menuId, tab);
        return this;
    }

    public Tab<T> getCurrentTab() {
        return currentTab;
    }

    public boolean performClickMenu(int menuId) {
        Tab<T> tTab = tabs.get(menuId);
        if (tTab != null) {
            doSelect(tTab);
            return true;
        }


        return false;
    }

    private void doSelect(Tab<T> tab) {
        Tab<T> oldTab = null;
        if (currentTab != null) {
            oldTab = currentTab;
            if (oldTab == tab) {
                notifyReselect(tab);
                return;
            }
        }
        currentTab = tab;
        doTabChanged(currentTab, oldTab);
    }

    private void doTabChanged(Tab<T> newTab, Tab<T> oldTab) {
        FragmentTransaction ft = fragmentManager.beginTransaction();
        if (oldTab != null) {
            if (oldTab.fragment != null) {
                // 从界面中移除，但是还在Fragment的缓存空间中
                ft.detach(oldTab.fragment);
            }
        }
        if (newTab != null) {
            if (newTab.fragment == null) {
                // 首次新建
                Fragment fragment = Fragment.instantiate(context, newTab.clx.getName(), null);
                // 缓存起来
                newTab.fragment = fragment;
                ft.add(containerId, fragment, newTab.clx.getName());
            } else {
                ft.attach(newTab.fragment);
            }
        }
        ft.commit();
        notifyTabSelect(newTab, oldTab);
    }

    /**
     * 回调监听器
     *
     * @param newTab
     * @param oldTab
     */
    private void notifyTabSelect(Tab<T> newTab, Tab<T> oldTab) {
        if (listener != null) {
            listener.onTabChanged(newTab, oldTab);
        }
    }

    private void notifyReselect(Tab<T> tab) {

    }

    public static class Tab<T> {
        public Class<?> clx;
        public T extra;
        // 内部缓存对应的fragment
        private Fragment fragment;

        public Tab(Class<?> clx, T extra) {
            this.clx = clx;
            this.extra = extra;
        }
    }

    /**
     * 定义事件处理完后的回调接口
     *
     * @param <T>
     */
    public interface OnTabChangedListener<T> {
        void onTabChanged(Tab<T> newTab, Tab<T> oldTab);
    }
}
