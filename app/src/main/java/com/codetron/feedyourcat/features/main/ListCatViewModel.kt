package com.codetron.feedyourcat.features.main

import android.content.Context
import androidx.lifecycle.*
import com.codetron.feedyourcat.database.CatDao
import com.codetron.feedyourcat.database.FeedYourCatDatabase
import com.codetron.feedyourcat.model.Cat
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

    fun getAllCats() = viewModelScope.launch {
        _loading.value = true
        withContext(Dispatchers.IO) {
            dao.getAll().collect {
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