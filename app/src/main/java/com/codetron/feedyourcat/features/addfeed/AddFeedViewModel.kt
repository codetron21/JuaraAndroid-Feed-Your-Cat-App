package com.codetron.feedyourcat.features.addfeed

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.codetron.feedyourcat.model.CatSelectedItem

class AddFeedViewModel() : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _listCats = MutableLiveData<List<CatSelectedItem>>()
    val listCats: LiveData<List<CatSelectedItem>> get() = _listCats

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun factory(context: Context) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AddFeedViewModel() as T
            }
        }
    }
}