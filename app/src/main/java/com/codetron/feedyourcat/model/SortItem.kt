package com.codetron.feedyourcat.model

import androidx.annotation.StringRes

data class SortItem(
    val id: Long,
    @StringRes val title: Int,
    val isSelected: Boolean = false
)