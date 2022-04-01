package com.codetron.feedyourcat.common.resources

import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.model.SortItem

object ItemSortResources {

    fun getResources(): List<SortItem> {
        return listOf(
            SortItem(0L, R.string.sort_newest, true),
            SortItem(1L, R.string.sort_latest),
            SortItem(2L, R.string.sort_name)
        )
    }

}