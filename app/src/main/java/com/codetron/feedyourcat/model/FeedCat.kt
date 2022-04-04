package com.codetron.feedyourcat.model

data class FeedCat(
    val catId: Long,
    val feedId: Long,
    val name: String,
    val photo: String,
    val times: List<Int>
)