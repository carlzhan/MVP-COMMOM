package com.zhan.carl.common.widget.recycler;

/**
 * Created by carlzhan on 2017/11/19.
 */

public interface AdapterCallback<T> {
    void upData(T data , RecyclerAdapter.ViewHolder<T> viewHolder);
}
