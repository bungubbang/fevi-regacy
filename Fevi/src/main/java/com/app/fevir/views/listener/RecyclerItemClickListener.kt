package com.app.fevir.views.listener

import android.support.v7.widget.RecyclerView

interface RecyclerItemClickListener {
    fun onItemClick(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>, position: Int)
}
