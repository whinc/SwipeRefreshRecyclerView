package com.whinc.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by whinc on 2015/12/19.
 */
public class SwipeRefreshRecyclerView extends SwipeRefreshLayout implements MyAdapterDataObserver.OnAdapterDataChangedListener{
    private static final String TAG = SwipeRefreshRecyclerView.class.getSimpleName();
    private RecyclerView mRecyclerView = null;
    private FrameLayout mFrameLayout;
    private RecyclerView.AdapterDataObserver mAdapterDataObserver;

    public View getEmptyView() {
        return mEmptyView;
    }

    public View setEmptyView(@LayoutRes int layoutRes) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(layoutRes, mFrameLayout, false);
        setEmptyView(view);
        return view;
    }

    public void setEmptyView(View emptyView) {
        final RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter == null) {
            throw new IllegalStateException("RecyclerView adapter is null");
        }

        if (mEmptyView != null) {
            mFrameLayout.removeView(mEmptyView);
            mEmptyView = null;
            if (mAdapterDataObserver != null) {
                adapter.unregisterAdapterDataObserver(mAdapterDataObserver);
            }
        }

        if (emptyView == null) {
            return;
        }

        mFrameLayout.addView(emptyView, 0);
        mEmptyView = emptyView;

        // 监听数据变化
        mAdapterDataObserver = new MyAdapterDataObserver(this);
        adapter.registerAdapterDataObserver(mAdapterDataObserver);
    }

    private View mEmptyView = null;

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
        if (isInEditMode()) {
            TextView textView = new TextView(getContext());
            textView.setText(getClass().getName());
            addView(textView);
            setBackgroundColor(Color.GRAY);
            return;
        }
        // 布局容器
        mFrameLayout = new FrameLayout(getContext());
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        addView(mFrameLayout, params);

        // RecyclerView
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
        mFrameLayout.addView(mRecyclerView, layoutParams);

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
                mOnLoadMoreListener.onLoadMore();
            }
        }
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    @Override
    public void onChanged() {
        RecyclerView.Adapter adapter = mRecyclerView.getAdapter();
        if (adapter.getItemCount() <= 0) {  // RecyclerView is empty
            if (mEmptyView != null) {
                mEmptyView.setVisibility(View.VISIBLE);
            }
            mRecyclerView.setVisibility(View.INVISIBLE);
        } else {                            // RecyclerView has item(s)
            if (mEmptyView != null) {
                mEmptyView.setVisibility(View.INVISIBLE);
            }
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    public interface OnLoadMoreListener {
        void onLoadMore();
    }
}
