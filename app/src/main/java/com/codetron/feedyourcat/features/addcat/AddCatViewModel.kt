package com.codetron.feedyourcat.features.addcat

import android.content.Context
import android.net.Uri
import androidx.lifecycle.*
import com.codetron.feedyourcat.R
import com.codetron.feedyourcat.database.CatDao
import com.codetron.feedyourcat.database.FeedYourCatDatabase
import com.codetron.feedyourcat.model.Cat
import com.codetron.feedyourcat.model.StateCat
import com.codetron.feedyourcat.utils.Event
import com.codetron.feedyourcat.utils.LiveEvent
import com.codetron.feedyourcat.utils.MutableLiveEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class AddCatViewModel(private val dao: CatDao) : ViewModel() {

    var state: StateCat = StateCat.ADD
        private set

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _message = MutableLiveEvent<Int>()
    val message: LiveEvent<Int> get() = _message

    private val _showDeleteDialog = MutableLiveEvent<Unit>()
    val showDeleteDialog: LiveEvent<Unit> get() = _showDeleteDialog

    private val _showCancelEditDialog = MutableLiveEvent<Unit>()
    val showCancelEditDialog: LiveEvent<Unit> get() = _showCancelEditDialog

    private val _showExitDialog = MutableLiveEvent<Unit>()
    val showExitDialog: LiveEvent<Unit> get() = _showExitDialog

    private val _isEdited = MutableLiveData(false)
    val isEdited: LiveData<Boolean> get() = _isEdited

    private val _data = MutableLiveData<Cat?>()
    val data: LiveData<Cat?> get() = _data

    private val _date = MutableLiveData<Date?>(null)
    val date: LiveData<Date?> get() = _date

    private val _imageUri = MutableLiveData<Uri?>(null)
    val imageUri: LiveData<Uri?> get() = _imageUri

    fun setDate(date: Date?) {
        _date.value = date
    }

    fun setImageUri(imageUri: Uri?) {
        _imageUri.value = imageUri
    }

    fun getDataById(id: Long) = viewModelScope.launch {
        _loading.value = true
        _data.value = withContext(Dispatchers.IO) { dao.getById(id) }
        _loading.value = false
    }

    fun upsertData(cat: Cat) = viewModelScope.launch {
        _loading.value = true
        val result = withContext(Dispatchers.IO) { dao.upsert(cat) }
        if (result > 0) {
            _message.value = when (state) {
                StateCat.ADD -> Event(R.string.message_success_add_data)
                StateCat.UPDATE -> Event(R.string.message_success_update_data)
            }
        } else {
            _message.value = when (state) {
                StateCat.ADD -> Event(R.string.message_fail_add_data)
                StateCat.UPDATE -> Event(R.string.message_fail_update_data)
            }
        }
        _loading.value = false
    }

    fun deleteDataById(id: Long) = viewModelScope.launch {
        _loading.value = true
        withContext(Dispatchers.IO) { dao.deleteById(id) }
        _loading.value = false
    }

    fun setStateCat(state: StateCat) {
        this.state = state
    }

    fun onMenuClicked() {
        val currentValue = _isEdited.value ?: false
        if (currentValue) {
            _showCancelEditDialog.value = Event(Unit)
        } else {
            _isEdited.value = true
        }
    }

    fun onCancelEditClicked() {
        _isEdited.value = false
    }

    fun onButtonDeleteClicked() {
        _showDeleteDialog.value = Event(Unit)
    }

    fun onButtonExitClicked() {
        _showExitDialog.value = Event(Unit)
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun factory(context: Context) = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return AddCatViewModel(
                    FeedYourCatDatabase
                        .getInstance(context)
                        .catDao()
                ) as T
            }
        }

    }

}