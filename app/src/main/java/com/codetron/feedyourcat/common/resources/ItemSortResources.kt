package com.codetron.feedyourcat.common.resources

import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.model.SortCategory
import com.codetron.feedyourcat.model.SortItem

object ItemSortResources {

    fun getResources(): List<SortItem> {
        return listOf(
            SortItem(SortCategory.NEWEST.id, R.string.sort_newest, true),
            SortItem(SortCategory.LATEST.id, R.string.sort_latest),
            SortItem(SortCategory.NAME.id, R.string.sort_name)
        )
    }

}