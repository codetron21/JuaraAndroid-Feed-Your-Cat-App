package com.codetron.feedyourcat.model

import java.time.LocalDateTime

data class CatItem(
    val id: Long,
    val name: String,
    val dateAdded: LocalDateTime
)