package com.zhan.carl.italker;

import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnticipateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.ViewTarget;
import com.zhan.carl.common.app.Activity;
import com.zhan.carl.common.widget.ProtraitView;
import com.zhan.carl.italker.frags.main.ActiveFragment;
import com.zhan.carl.italker.frags.main.ContactFragment;
import com.zhan.carl.italker.frags.main.GroupFragment;
import com.zhan.carl.italker.helper.NavHelper;

import net.qiujuer.genius.ui.Ui;
import net.qiujuer.genius.ui.widget.FloatActionButton;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends Activity implements BottomNavigationView.OnNavigationItemSelectedListener, NavHelper.OnTabChangedListener<Integer> {
    @BindView(R.id.appbar)
    View mLayAppbar;
    @BindView(R.id.im_protraitView)
    ProtraitView mProtraitView;
    @BindView(R.id.txt_title)
    TextView mTitle;

    @BindView(R.id.layout_container)
    FrameLayout mContainer;
    @BindView(R.id.navigation)
    BottomNavigationView mNavigation;
    private NavHelper mNavHelper;
    @BindView(R.id.btn_action)
    FloatActionButton mBtnAction;

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initWidget() {
        super.initWidget();
        mNavHelper = new NavHelper<Integer>(this, R.id.layout_container, getSupportFragmentManager(), this);
        mNavHelper.add(R.id.action_home, new NavHelper.Tab(ActiveFragment.class, R.string.title_home))
                .add(R.id.action_group, new NavHelper.Tab(GroupFragment.class, R.string.title_group))
                .add(R.id.action_contact, new NavHelper.Tab(ContactFragment.class, R.string.action_contact));
        mNavigation.setOnNavigationItemSelectedListener(this);
        Glide.with(this).load(R.drawable.bg_src_morning)
                .centerCrop()
                .into(new ViewTarget<View, GlideDrawable>(mLayAppbar) {
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                        this.view.setBackground(resource.getCurrent());
                    }
                });
    }

    @Override
    protected void initData() {
        super.initData();
        // 第一次触发menu
        Menu menu = mNavigation.getMenu();
        // 触发首次选中Home
        menu.performIdentifierAction(R.id.action_home, 0);
    }

    @OnClick(R.id.im_search)
    void onSearchMenuClick() {

    }

    @OnClick(R.id.btn_action)
    void onActionClick() {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return mNavHelper.performClickMenu(item.getItemId());
    }

    /**
     * 处理后ode回调方法
     *
     * @param newTab 新的tab
     * @param oldTab 旧的Tab
     */
    @Override
    public void onTabChanged(NavHelper.Tab<Integer> newTab, NavHelper.Tab<Integer> oldTab) {
        mTitle.setText(newTab.extra);
        float transY = 0;
        float rotation = 0;
        if (Objects.equals(newTab.extra, R.string.title_home)) {
            transY = Ui.dipToPx(getResources(), 76);
        } else {
            if (Objects.equals(newTab.extra, R.string.title_group)) {
                mBtnAction.setImageResource(R.drawable.ic_group_add);
                rotation = -360;
            } else {
                mBtnAction.setImageResource(R.drawable.ic_contact_add);
                rotation = 360;
            }
        }
        mBtnAction.animate()
                .rotation(rotation)
                .translationY(transY)
                .setInterpolator(new AnticipateInterpolator(1))
                .setDuration(480)
                .start();
    }
}
