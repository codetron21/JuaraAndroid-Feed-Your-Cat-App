package com.codetron.feedyourcat.features.main.cat

import android.content.Context
import androidx.lifecycle.*
import com.codetron.feedyourcat.database.FeedYourCatDatabase
import com.codetron.feedyourcat.database.dao.CatDao
import com.codetron.feedyourcat.model.Cat
import com.codetron.feedyourcat.model.SortCategory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListCatViewModel(
    private val dao: CatDao
) : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _data = MutableLiveData<List<Cat>>()
    val data: LiveData<List<Cat>> get() = _data

    fun getAll(id: Long) {
        when (id) {
            SortCategory.NEWEST.id -> {
                getData(dao.getAllSortByNewest(), _data)
            }
            SortCategory.LATEST.id -> {
                getData(dao.getAllSortByLatest(), _data)
            }
            SortCategory.NAME.id -> {
                getData(dao.getAllSortByName(), _data)
            }
            else -> {
                getData(dao.getAllSortByNewest(), _data)
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

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun factory(context: Context) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return ListCatViewModel(FeedYourCatDatabase.getInstance(context).catDao()) as T
            }
        }
    }

}