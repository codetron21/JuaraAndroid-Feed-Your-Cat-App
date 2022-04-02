package com.codetron.feedyourcat.features.main

import android.content.Context
import androidx.lifecycle.*
import com.codetron.feedyourcat.database.CatDao
import com.codetron.feedyourcat.database.FeedYourCatDatabase
import com.codetron.feedyourcat.model.Cat
import com.codetron.feedyourcat.model.SortCategory
import kotlinx.coroutines.Dispatchers
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
                getAllCatsSortNewest()
            }
            SortCategory.LATEST.id -> {
                getAllCatsSortLatest()
            }
            SortCategory.NAME.id -> {
                getAllCatsSortName()
            }
            else -> {
                getAllCatsSortNewest()
            }
        }
    }

    fun getAllCatsSortNewest() = viewModelScope.launch {
        _loading.value = true
        withContext(Dispatchers.IO) {
            dao.getAllSortByNewest().collect {
                withContext(Dispatchers.Main) {
                    _data.value = it
                    _loading.value = false
                }
            }
        }
    }

    fun getAllCatsSortLatest() = viewModelScope.launch {
        _loading.value = true
        withContext(Dispatchers.IO) {
            dao.getAllSortByLatest().collect {
                withContext(Dispatchers.Main) {
                    _data.value = it
                    _loading.value = false
                }
            }
        }
    }

    fun getAllCatsSortName() = viewModelScope.launch {
        _loading.value = true
        withContext(Dispatchers.IO) {
            dao.getAllSortByName().collect {
                withContext(Dispatchers.Main) {
                    _data.value = it
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