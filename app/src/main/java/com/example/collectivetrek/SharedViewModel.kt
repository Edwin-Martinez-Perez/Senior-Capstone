package com.example.collectivetrek

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _groupIdSetResult = MutableLiveData<Boolean>()
    val groupIdSetResult: LiveData<Boolean> get() = _groupIdSetResult
    private val _sharedGroupId = MutableLiveData<String>()
    val sharedGroupId: LiveData<String>
        get() = _sharedGroupId

    fun setGroupId(newGroupId: String) {
        _sharedGroupId.value = newGroupId
        _groupIdSetResult.postValue(true)
    }
}