package com.codetron.feedyourcat2.model

data class FeedCat(
    val catId: Long,
    val feedId: Long,
    val name: String,
    val photo: String,
    val times: List<Int>
)