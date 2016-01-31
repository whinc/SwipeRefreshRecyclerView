package com.whinc.widget;

import android.support.v7.widget.RecyclerView;

/**
 * Created by Administrator on 2016/1/31.
 */
class MyAdapterDataObserver extends RecyclerView.AdapterDataObserver {


    private OnAdapterDataChangedListener mListener;
    public MyAdapterDataObserver(OnAdapterDataChangedListener listener) {
        mListener = listener;
    }

    @Override
    public void onChanged() {
        super.onChanged();
        dispatchChange();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        super.onItemRangeChanged(positionStart, itemCount);
        dispatchChange();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        super.onItemRangeChanged(positionStart, itemCount, payload);
        dispatchChange();
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        super.onItemRangeInserted(positionStart, itemCount);
        dispatchChange();
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        super.onItemRangeRemoved(positionStart, itemCount);
        dispatchChange();
    }

    private void dispatchChange() {
        if (mListener != null) {
            mListener.onChanged();
        }
    }

    interface OnAdapterDataChangedListener {
        void onChanged();
    }
}

