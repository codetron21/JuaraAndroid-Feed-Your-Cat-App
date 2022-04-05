package com.codetron.feedyourcat.common.adapter

import androidx.recyclerview.selection.ItemKeyProvider

class FeedCatItemKeyProvider(
    private val adapter: ListFeedCatAdapter
) : ItemKeyProvider<Long>(SCOPE_CACHED) {

    override fun getKey(position: Int): Long {
        return adapter.currentList[position].feedId
    }

    override fun getPosition(key: Long): Int {
        return adapter.currentList.indexOfFirst { it.feedId == key }
    }
}