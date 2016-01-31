package com.whinc.test.swiperefreshrecyclerview;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.whinc.widget.SwipeRefreshRecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private MyAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Find view
        findViewById(R.id.add_items_btn).setOnClickListener(this);
        findViewById(R.id.clear_all_btn).setOnClickListener(this);
        final SwipeRefreshRecyclerView swipeRefreshRecyclerView
                = (SwipeRefreshRecyclerView) findViewById(R.id.swipe_refresh_recycler_view);

        // Set adapter to RecyclerView
        final RecyclerView recyclerView = swipeRefreshRecyclerView.getRecyclerView();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyAdapter();
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

        View emptyView = swipeRefreshRecyclerView.setEmptyView(R.layout.include_empty_view);
        emptyView.findViewById(R.id.retry_btn).setOnClickListener(this);

        // Listener to refresh event
        swipeRefreshRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(MainActivity.this, "Refresh begin", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshRecyclerView.setRefreshing(false);
                        Toast.makeText(MainActivity.this, "Refresh end", Toast.LENGTH_SHORT).show();
                        swipeRefreshRecyclerView.getRecyclerView().smoothScrollToPosition(99);
                    }
                }, 3000);
            }
        });

        // Listener to load more event
        swipeRefreshRecyclerView.setOnLoadMoreListener(new SwipeRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Toast.makeText(MainActivity.this, "load more begin", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshRecyclerView.setLoading(false);
                        Toast.makeText(MainActivity.this, "load more finish", Toast.LENGTH_SHORT).show();
                        recyclerView.smoothScrollToPosition(0);
                    }
                }, 3000);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_items_btn:
                for (int i = 0; i < 100; ++i) {
                    mAdapter.add(String.valueOf(i));
                }
                break;
            case R.id.clear_all_btn:
                mAdapter.clear();
                break;
            case R.id.retry_btn:
                Toast.makeText(MainActivity.this, "Nothing to show", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

        private List<String> mData = new ArrayList<>();

        public MyAdapter() {
        }

        public void add(String item) {
            mData.add(item);
            notifyItemInserted(mData.size());
        }

        public void clear() {
            mData.clear();
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            TextView textView = new TextView(parent.getContext());
            return new ViewHolder(textView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            TextView textView = (TextView) holder.itemView;
            textView.setText(mData.get(position));
        }

        @Override
        public int getItemCount() {
            return mData.size();
        }

        static class ViewHolder extends RecyclerView.ViewHolder {

            public ViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
