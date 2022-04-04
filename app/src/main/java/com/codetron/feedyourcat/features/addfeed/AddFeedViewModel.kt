package com.codetron.feedyourcat.features.addfeed

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.database.FeedYourCatDatabase
import com.codetron.feedyourcat.database.dao.FeedCatDao
import com.codetron.feedyourcat.database.dao.FeedDao
import com.codetron.feedyourcat.model.CatSelectedItem
import com.codetron.feedyourcat.model.Feed
import com.codetron.feedyourcat.model.Time
import com.codetron.feedyourcat.utils.Event
import com.codetron.feedyourcat.utils.LiveEvent
import com.codetron.feedyourcat.utils.MutableLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "AddFeedViewModel"

class AddFeedViewModel(
    private val feedDao: FeedDao,
    private val feedCatDao: FeedCatDao
) : ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean> get() = _loading

    private val _listCats = MutableLiveData<List<CatSelectedItem>>()
    val listCats: LiveData<List<CatSelectedItem>> get() = _listCats

    private val _listTime = MutableLiveData<List<Time>>()
    val listTime: LiveData<List<Time>> get() = _listTime

    private val _showExitDialog = MutableLiveEvent<Unit>()
    val showExitDialog: LiveEvent<Unit> get() = _showExitDialog

    private val _showTimeDialog = MutableLiveEvent<Unit>()
    val showTimeDialog: LiveEvent<Unit> get() = _showTimeDialog

    private val _showMessage = MutableLiveEvent<Int>()
    val showMessage: LiveEvent<Int> get() = _showMessage

    init {
        getAllCat()
    }

    fun getAllCat() = viewModelScope.launch {
        _loading.value = true
        withContext(Dispatchers.IO) {
            feedCatDao.getAllCatAvailable().collect { list ->
                withContext(Dispatchers.Main) {
                    Log.d(TAG, "$list")
                    _listCats.value = list.map { cat ->
                        CatSelectedItem(cat, false)
                    }
                    _loading.value = false
                }
            }
        }
    }

    fun setSelectedCat(id: Long) {
        val currentListCat = _listCats.value ?: emptyList()
        val updatedListCat = currentListCat.map {
            it.copy(isSelected = it.cat.id == id)
        }
        _listCats.value = updatedListCat
    }

    fun removeTimeItem(id: Long) {
        val currentList = _listTime.value ?: emptyList()
        val newListTime = currentList.filter { it.id != id }
        _listTime.value = newListTime
    }

    fun onButtonSubmitClicked(finish: () -> Unit) = viewModelScope.launch {
        val selectedCat = _listCats.value?.find { it.isSelected }
        if (selectedCat == null) {
            _showMessage.value = Event(R.string.message_choose_cat)
            return@launch
        }

        val listTime = _listTime.value ?: emptyList()
        if (listTime.isEmpty()) {
            _showMessage.value = Event(R.string.message_time_empty)
            return@launch
        }

        val feed = Feed(listTime.map { it.time }, selectedCat.cat.id)
        Log.d(TAG, "FEED: $feed")
        withContext(Dispatchers.IO) {
            val result = feedDao.upsert(feed)
            withContext(Dispatchers.Main) {
                _showMessage.value = if (result > 0) {
                    Event(R.string.message_success_save_feed)
                } else {
                    Event(R.string.message_failed_save_feed)
                }
                finish()
            }
        }
    }

    fun onButtonAddClicked() {
        _showTimeDialog.value = Event(Unit)
    }

    fun onButtonExitClicked() {
        _showExitDialog.value = Event(Unit)
    }

    fun setTime(time: Int) {
        val timeObj = Time(System.currentTimeMillis(), time)
        val currentList = _listTime.value ?: emptyList()
        val newListTime = listOf(*currentList.toTypedArray(), timeObj)
        _listTime.value = newListTime
    }

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun factory(context: Context) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val db = FeedYourCatDatabase.getInstance(context)
                return AddFeedViewModel(db.feedDao(), db.feedCatDao()) as T
            }
        }
    }
}