package com.example.collectivetrek

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _sharedString = MutableLiveData<String>()
    val sharedGroupId: LiveData<String>
        get() = _sharedString

    fun updateGroupId(newGroupId: String) {
        _sharedString.value = newGroupId
    }
}