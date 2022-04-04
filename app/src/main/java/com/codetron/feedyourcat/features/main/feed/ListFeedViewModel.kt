package com.codetron.feedyourcat.features.main.feed

import android.content.Context
import androidx.lifecycle.*
import com.codetron.feedyourcat.database.FeedYourCatDatabase
import com.codetron.feedyourcat.database.dao.CatDao
import com.codetron.feedyourcat.database.dao.FeedCatDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ListFeedViewModel(
    private val feedCatDao: FeedCatDao,
    private val catDao: CatDao
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
            val cats = catDao.getAllSortByName()
            val feedCat = feedCatDao.getAllCatAvailable()

            combine(cats, feedCat) { c, fc ->
                fc.ifEmpty { c }
            }.collect {
                withContext(Dispatchers.Main) {
                    _showAddButton.value = it.isNotEmpty()
                }
            }
        }
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun factory(context: Context) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val db = FeedYourCatDatabase.getInstance(context)
                return ListFeedViewModel(
                    db.feedCatDao(),
                    db.catDao()
                ) as T
            }
        }
    }

}