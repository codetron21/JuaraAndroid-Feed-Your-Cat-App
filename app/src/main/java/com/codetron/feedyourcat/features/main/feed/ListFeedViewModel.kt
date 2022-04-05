package com.codetron.feedyourcat.features.main.feed

import android.content.Context
import androidx.lifecycle.*
import com.codetron.feedyourcat.database.FeedYourCatDatabase
import com.codetron.feedyourcat.database.dao.FeedCatDao
import com.codetron.feedyourcat.database.dao.FeedDao
import com.codetron.feedyourcat.model.FeedCat
import com.codetron.feedyourcat.model.SortCategory
import com.codetron.feedyourcat.utils.Event
import com.codetron.feedyourcat.utils.LiveEvent
import com.codetron.feedyourcat.utils.MutableLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListFeedViewModel(
    private val feedDao: FeedDao,
    private val feedCatDao: FeedCatDao,
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _showAddButton = MutableLiveData<Boolean>()
    val showAddButton: LiveData<Boolean> get() = _showAddButton

    private val _data = MutableLiveData<List<FeedCat>>()
    val data: LiveData<List<FeedCat>> get() = _data

    private val _message = MutableLiveEvent<Int>()
    val message: LiveEvent<Int> get() = _message

    init {
        getIsCatAvailable()
    }

    fun getAll(id: Long) {
        when (id) {
            SortCategory.NEWEST.id -> {
                getData(feedCatDao.getAllSortByNewest(), _data)
            }
            SortCategory.LATEST.id -> {
                getData(feedCatDao.getAllSortByLatest(), _data)
            }
            SortCategory.NAME.id -> {
                getData(feedCatDao.getAllSortByName(), _data)
            }
            else -> {
                getData(feedCatDao.getAllSortByNewest(), _data)
            }
        }
    }

    fun <T> getData(
        dataSort: Flow<T>,
        mutableLiveData: MutableLiveData<T>
    ) = viewModelScope.launch {
        _loading.value = true
        withContext(Dispatchers.IO) {
            dataSort.collect {
                withContext(Dispatchers.Main) {
                    mutableLiveData.value = it
                    _loading.value = false
                }
            }
        }
    }

    fun getIsCatAvailable() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            feedCatDao.getAllCatAvailable().collect {
                withContext(Dispatchers.Main) {
                    _showAddButton.value = it.isNotEmpty()
                }
            }
        }
    }

    fun deleteListData(selectedList: List<FeedCat>) = viewModelScope.launch {
        _loading.value = true
        withContext(Dispatchers.IO) {
            feedDao.deleteByIds(selectedList.map { it.feedId })
            withContext(Dispatchers.Main) {
                _message.value = Event(selectedList.size)
            }
        }
        _loading.value = false
    }


    companion object {
        @Suppress("UNCHECKED_CAST")
        fun factory(context: Context) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val db = FeedYourCatDatabase.getInstance(context)
                return ListFeedViewModel(
                    db.feedDao(),
                    db.feedCatDao(),
                ) as T
            }
        }
    }

}