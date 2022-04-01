package com.codetron.feedyourcat.features.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codetron.feedyourcat.common.resources.ItemSortResources
import com.codetron.feedyourcat.model.SortItem
import com.codetron.feedyourcat.utils.Event
import com.codetron.feedyourcat.utils.MutableLiveEvent


class MainViewModel(dataResources: List<SortItem>) : ViewModel() {

    private val _listSortCats = MutableLiveData(dataResources)
    private val _listSortFeed = MutableLiveData(dataResources)
    private val _showDialog = MutableLiveEvent<Unit>()

    var state = State.FEED
        private set

    val textSortCats = Transformations.map(_listSortCats) {
        it.first { i -> i.isSelected }.title
    }

    val textSortFeed = Transformations.map(_listSortFeed) {
        it.first { i -> i.isSelected }.title
    }

    val showDialog = Transformations.map(_showDialog) { event ->
        event.get() ?: emptyList<SortItem>()
        when (state) {
            State.FEED -> _listSortFeed.value ?: emptyList()
            State.CATS -> _listSortCats.value ?: emptyList()
        }
    }

    fun setSelectedSortFeed(id: Long) {
        _listSortFeed.value = _listSortFeed.value?.map { it.copy(isSelected = it.id == id) }
    }

    fun setSelectedSortCats(id: Long) {
        _listSortCats.value = _listSortCats.value?.map { it.copy(isSelected = it.id == id) }
    }

    fun onButtonSortFeedClicked() {
        state = State.FEED
        _showDialog.value = Event(Unit)
    }

    fun onButtonSortCatsClicked() {
        state = State.CATS
        _showDialog.value = Event(Unit)
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainViewModel(ItemSortResources.getResources()) as T
            }
        }

        enum class State {
            FEED,
            CATS
        }
    }
}