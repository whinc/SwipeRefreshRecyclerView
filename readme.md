### swipe-refresh-recycler-view

A `RecyclerView` that implements pull refresh and load more. 

一个实现了下拉刷新和上拉加载更多的`RecyclerView`。

![screenshot](./screenshot.gif)

### Integration 集成

```groovy
repositories {
    ...
    maven { url "https://jitpack.io" }
}

dependencies {
    ...
    compile 'com.github.whinc:swipe-refresh-recycler-view:1.0.0'
}
```

### How to use 使用

```java
// Find view
final SwipeRefreshRecyclerView refreshRecyclerView
        = (SwipeRefreshRecyclerView) findViewById(R.id.swipe_refresh_recycler_view);

// Listener to refresh event
refreshRecyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
    @Override
    public void onRefresh() {
        Toast.makeText(MainActivity.this, "Refresh begin", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshRecyclerView.setRefreshing(false);
                Toast.makeText(MainActivity.this, "Refresh end", Toast.LENGTH_SHORT).show();
            }
        }, 3000);
    }
});

// Set adapter to RecyclerView
RecyclerView recyclerView = refreshRecyclerView.getRecyclerView();
recyclerView.setLayoutManager(new LinearLayoutManager(this));
recyclerView.setAdapter(new MyAdapter());

// Listener to load more event
refreshRecyclerView.setOnLoadMoreListener(new SwipeRefreshRecyclerView.OnLoadMoreListener() {
    @Override
    public void loadMore() {
        Toast.makeText(MainActivity.this, "load more begin", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshRecyclerView.finishLoading();
                Toast.makeText(MainActivity.this, "load more finish", Toast.LENGTH_SHORT).show();
            }
        }, 3000);
    }
});
```