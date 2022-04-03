package com.codetron.feedyourcat.features.main.feed

import android.content.Context
import androidx.lifecycle.*
import com.codetron.feedyourcat.database.FeedYourCatDatabase
import com.codetron.feedyourcat.database.dao.FeedCatDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListFeedViewModel(
    private val feedCatDao: FeedCatDao
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _showAddButton = MutableLiveData<Boolean>()
    val showAddButton: LiveData<Boolean> get() = _showAddButton

    init {
        getIsCatAvailable()
    }

    fun getIsCatAvailable() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            val feed = feedCatDao.isFeedAvailable()
            val cat = feedCatDao.isCatAvailable()
            val feedCat = feedCatDao.isFeedCatAvailable()

            combine(feed, cat, feedCat) { f, c, fc ->
                if (!f && c) true
                else fc
            }.collect {
                withContext(Dispatchers.Main) {
                    _showAddButton.value = it
                }
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun factory(context: Context) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ListFeedViewModel(
                    FeedYourCatDatabase.getInstance(context).feedCatDao()
                ) as T
            }
        }
    }

}