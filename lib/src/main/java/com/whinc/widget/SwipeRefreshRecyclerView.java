package com.whinc.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by whinc on 2015/12/19.
 */
public class SwipeRefreshRecyclerView extends SwipeRefreshLayout
        implements GestureDetector.OnGestureListener{
    private static final String TAG = SwipeRefreshRecyclerView.class.getSimpleName();
    private RecyclerView mRecyclerView = null;

    public boolean isLoading() {
        return mLoading;
    }

    public void finishLoading() {
        mLoading = false;
    }

    private boolean mLoading = false;

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    private OnLoadMoreListener mOnLoadMoreListener;

    public SwipeRefreshRecyclerView(Context context) {
        super(context);
        init(null);
    }

    public SwipeRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mRecyclerView = new RecyclerView(getContext());
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(getContext(), attrs);
        addView(mRecyclerView, layoutParams);

        final GestureDetector gestureDetector = new GestureDetector(getContext(), this);
        mRecyclerView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gestureDetector.onTouchEvent(event);
                return false;
            }
        });
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        boolean scrollUp = (distanceY > 0);
        if (!scrollUp || isRefreshing() || isLoading() || mOnLoadMoreListener == null) {
            return false;
        }

        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        int itemCount = layoutManager.getItemCount();
        if (layoutManager instanceof LinearLayoutManager) {
            LinearLayoutManager llm = (LinearLayoutManager) layoutManager;
            int pos = llm.findLastVisibleItemPosition();
            if (pos == itemCount - 1) {
                mLoading = true;
                mOnLoadMoreListener.loadMore();
            }
        }
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }
}
