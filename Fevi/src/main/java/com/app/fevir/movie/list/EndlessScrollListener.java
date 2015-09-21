package com.app.fevir.movie.list;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class EndlessScrollListener extends RecyclerView.OnScrollListener {

    private int visibleThreshold = 3;
    private OnMoreListener onMoreListener;

    public EndlessScrollListener(int visibleThreshold, OnMoreListener onMoreListener) {
        this.visibleThreshold = visibleThreshold;
        this.onMoreListener = onMoreListener;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        int itemCount = recyclerView.getAdapter().getItemCount();

        if (itemCount - lastVisibleItemPosition <= visibleThreshold) {
            if (onMoreListener != null) {
                onMoreListener.onMore();
            }
        }
    }

    interface OnMoreListener {
        void onMore();
    }
}
