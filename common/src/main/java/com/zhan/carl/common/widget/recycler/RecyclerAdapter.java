package com.zhan.carl.common.widget.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhan.carl.common.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by carlzhan on 2017/11/19.
 */

public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder<T>> implements
        View.OnClickListener, View.OnLongClickListener, AdapterCallback<T> {

    private final List<T> mDataList;
    private AdapterListener<T> mListener;

    /**
     * 构造函数
     */
    public RecyclerAdapter() {
        this(null);
    }

    /**
     * 构造函数
     */
    public RecyclerAdapter(AdapterListener<T> listener) {
        this(new ArrayList<T>(), listener);
    }

    /**
     * 构造函数
     */
    public RecyclerAdapter(List<T> dataList, AdapterListener<T> listener) {
        this.mDataList = dataList;
        this.mListener = listener;
    }

    /**
     * 创建一个ViewHolder
     *
     * @param parent   RecyclerView
     * @param viewType 界面类型
     * @return ViewHolder
     */
    @Override
    public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        // 将xml （id为viewType）的文件初始化为一个root View
        View root = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        // 通过子类必须实现的方法，得到一个ViewHolder
        ViewHolder<T> holder = onCreateViewHolder(root, viewType);
        // 设置View的Tag为ViewHolder，进行双向绑定  通过tag获取ViewHolder
        root.setTag(R.id.tag_recycler_holder, holder);
        // 设置view的点击事件
        root.setOnClickListener(this);
        root.setOnLongClickListener(this);
        // 进行界面注解绑定
        holder.unbinder = ButterKnife.bind(holder, root);
        // 绑定callback
        holder.callback = this;
        return holder;
    }

    @Override
    public int getItemViewType(int position) {
        return getItemViewType(position, mDataList.get(position));
    }

    protected abstract int getItemViewType(int position, T data);

    protected abstract ViewHolder<T> onCreateViewHolder(View view, int viewType);

    /**
     * 绑定数据到一个ViewHolder上
     *
     * @param holder   ViewHolder
     * @param position 坐标
     */
    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position) {
        // 得到需要绑定的数据
        T data = mDataList.get(position);
        // 绑定数据
        holder.bind(data);
    }


    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    /**
     * 插入一条数据并通知插入
     *
     * @param data
     */
    public void add(T data) {
        mDataList.add(data);
//        notifyDataSetChanged();
        notifyItemInserted(mDataList.size() - 1);
    }

    /**
     * 插入一堆数据，并通知这段集合更新
     *
     * @param dataList
     */
    public void addAll(T... dataList) {
        if (dataList != null && dataList.length > 0) {
            int startPos = mDataList.size();
            Collections.addAll(mDataList, dataList);
            notifyItemRangeInserted(startPos, dataList.length);
        }
    }

    /**
     * 插入一堆数据，并通知这段集合更新
     *
     * @param dataList
     */
    public void addAll(Collection<T> dataList) {
        if (dataList != null && dataList.size() > 0) {
            int startPos = mDataList.size();
            mDataList.addAll(dataList);
            notifyItemRangeInserted(startPos, dataList.size());
        }
    }

    /**
     * 删除操作
     */
    public void clean() {
        mDataList.clear();
        notifyDataSetChanged();
    }

    /**
     * 替换一个新的集合。其中包括了清空
     *
     * @param dataList 一个新的集合
     */
    public void replace(Collection<T> dataList) {
        mDataList.clear();
        if (dataList == null || dataList.size() == 0)
            return;
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }


    @Override
    public void onClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            // 得到ViewHolder当前对应的适配器中的坐标
            int pos = viewHolder.getAdapterPosition();
            // 回调方法
            this.mListener.onItemClick(viewHolder, mDataList.get(pos));
        }
    }

    @Override
    public boolean onLongClick(View v) {
        ViewHolder viewHolder = (ViewHolder) v.getTag(R.id.tag_recycler_holder);
        if (this.mListener != null) {
            // 得到ViewHolder当前对应的适配器中的坐标
            int pos = viewHolder.getAdapterPosition();
            // 回调方法
            this.mListener.onItemLongClick(viewHolder, mDataList.get(pos));
        }
        return false;
    }

    /**
     * 设置适配器监听
     *
     * @param adapterListener
     */
    public void setListerner(AdapterListener<T> adapterListener) {
        this.mListener = adapterListener;
    }

    /**
     * 自定义点击监听器
     *
     * @param <T>
     */
    public interface AdapterListener<T> {
        //  当Cell点击时触发
        void onItemClick(RecyclerAdapter.ViewHolder holder, T data);

        //  当Cell长按时触发
        void onItemLongClick(RecyclerAdapter.ViewHolder holder, T data);
    }


    public static abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

        protected T mData;
        private Unbinder unbinder;
        private AdapterCallback<T> callback;

        public ViewHolder(View itemView) {
            super(itemView);
        }

        /**
         * 用于绑定数据的触发
         *
         * @param data 绑定的数据
         */
        void bind(T data) {
            this.mData = data;
            onBind(mData);
        }

        /**
         * 当触发绑定数据的时候的回调，必须复写
         *
         * @param data 绑定的数据
         */
        protected abstract void onBind(T data);

        /**
         * Holder自己对自己对应的Data进行更新操作
         *
         * @param data
         */
        public void updataData(T data) {
            if (this.callback != null) {
                this.callback.upData(data, this);
            }
        }
    }
}
