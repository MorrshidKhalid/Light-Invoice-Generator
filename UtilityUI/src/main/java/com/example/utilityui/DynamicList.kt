package com.example.utilityui

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object DynamicList {

    // Prepare a RecyclerView for displaying data in a vertically scrolling list.
    fun setupVertically(rv: RecyclerView, context: Context) {
        rv.layoutManager = LinearLayoutManager(context)
        rv.setHasFixedSize(true)
    }

    // Prepare a RecyclerView for displaying data in a horizontally scrolling list.
}