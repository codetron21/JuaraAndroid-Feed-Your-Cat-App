package com.codetron.feedyourcat.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class Event<T>(private val data: T?) {

    private var isHandled: Boolean = false

    fun get(): T? {
        if (isHandled) return null

        isHandled = true
        return data
    }

    fun peek(): T? = data
}

typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>
typealias LiveEvent<T> = LiveData<Event<T>>