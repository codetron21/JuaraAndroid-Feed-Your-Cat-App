package com.codetron.feedyourcat.common.adapter

import android.view.MotionEvent
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.widget.RecyclerView

class FeedCatItemsDetailsLookup(
    private val rv: RecyclerView
) : ItemDetailsLookup<Long>() {

    override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {
        val view = rv.findChildViewUnder(e.x, e.y) ?: return null

        return (rv.getChildViewHolder(view) as ListFeedCatAdapter.FeedCatViewHolder).getItem()
    }
}