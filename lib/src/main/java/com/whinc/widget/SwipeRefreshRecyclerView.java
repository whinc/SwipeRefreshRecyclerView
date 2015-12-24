package com.whinc.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by whinc on 2015/12/19.
 */
public class SwipeRefreshRecyclerView extends SwipeRefreshLayout {
    private static final String TAG = SwipeRefreshRecyclerView.class.getSimpleName();
    private RecyclerView mRecyclerView = null;

    public boolean isLoading() {
        return mLoading;
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
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
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                onScrolledHandler(recyclerView, dx, dy);
            }
        });
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
        );
        addView(mRecyclerView, layoutParams);

    }

    private void onScrolledHandler(RecyclerView recyclerView, int dx, int dy) {
        boolean scrollUp = (dy > 0);
        if (!scrollUp || isRefreshing() || isLoading() || mOnLoadMoreListener == null) {
            return;
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
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public interface OnLoadMoreListener {
        void loadMore();
    }
}
