package com.codetron.feedyourcat.model

import androidx.room.Embedded
import androidx.room.Relation

data class FeedCat(
    @Embedded val cat: Cat?,
    @Relation(
        parentColumn = "id",
        entityColumn = "cat_id"
    )
    val feed: Feed?
)