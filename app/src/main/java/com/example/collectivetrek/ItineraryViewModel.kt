package com.example.collectivetrek

import android.net.Uri
import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import com.example.collectivetrek.database.Event
import com.example.collectivetrek.database.Filter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItineraryViewModel(private val repository: ItineraryRepository): ViewModel() {
    // TODO
    // get group id from the group page before coming to itinerary page
    //val groupid = "ABCDEFID4"
    //val groupid = "ABCDEFID3"
    //val groupid = "ABCDEFID2"
    //val groupid = "ABCDEFID1"


    private val _groupId = MutableLiveData<String>()
    val groupId: LiveData<String>
        get() = _groupId

    fun setGroupId(newGroupId: String) {
        Log.d("newGroupId", newGroupId)
        _groupId.value = newGroupId
        _groupIdSetResult.postValue(true)
    }

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> = _event

    private val _filter = MutableLiveData<Filter>()
    val filter: LiveData<Filter> = _filter

    var filters: LiveData<List<Filter>> = repository.getFilters(groupId.value.toString()){ result ->
        _filterShownResult.postValue(result)
    }


    var filteredEvents: LiveData<List<Event>> = repository.getFilteredEvents(groupId.value.toString(),filter.value?.id.toString()){ result->
        _filteredEventsShownResult.postValue(result)

    }



    private val _dataInsertionResult = MutableLiveData<Boolean>()
    val dataInsertionResult: LiveData<Boolean> get() = _dataInsertionResult

    private val _filterInsertionResult = MutableLiveData<Boolean>()
    val filterInsertionResult: LiveData<Boolean> get() = _filterInsertionResult

    private val _filterShownResult = MutableLiveData<Boolean>()
    val filterShownResult: LiveData<Boolean> get() = _filterShownResult

    private val _filteredEventsShownResult = MutableLiveData<Boolean>()
    val filteredEventsShownResult: LiveData<Boolean> get() = _filteredEventsShownResult

    private val _eventModificationResult = MutableLiveData<Boolean>()
    val eventModificationResult: LiveData<Boolean> get() = _eventModificationResult

    private val _filterDeletionResult = MutableLiveData<Boolean>()
    val filterDeletionResult: LiveData<Boolean> get() = _filterDeletionResult

    private val _groupIdSetResult = MutableLiveData<Boolean>()
    val groupIdSetResult: LiveData<Boolean> get() = _groupIdSetResult

    fun setDataInsertionResultFalse(){
        _dataInsertionResult.postValue(false)
    }

    //set event
    fun setEvent(event: Event){
        _event.value = event
    }
    fun setFilter(filter: Filter){
        Log.d("setFilter","before setting")
        _filter.value = filter
        Log.d("setFilter", filter.id.toString())
    }


    fun setFilters(groupId: String){
        Log.d("viewmodel","setFilters")
        Log.d("viewmodel in setFilters",groupId)
        filters = repository.getFilters(groupId){ result ->
            Log.d("viewmodel",result.toString())
            _filterShownResult.postValue(result)
        }
    }

    fun setFilteredEvents(){
        Log.d("set filter viewmodel",filter.value?.id.toString())
        filteredEvents = repository.getFilteredEvents(groupId.value.toString(),filter.value?.id.toString()){result->
            _filteredEventsShownResult.postValue(result)
        }
    }

    //get
    fun getEvent(): Event{
        return event.value!!
    }
    fun getFilter(): Filter{
        return filter.value!!
    }


    fun insertEvent(event: Event, filterId: String) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("insertEvent in viewmodel",filter.value?.id.toString())
        Log.d("insertEvent in viewmodel",filterId)
        Log.d("insertEvent in viewmodel",groupId.value.toString())
        repository.insertEvent(filterId,event, groupId.value.toString()){ result ->
            _dataInsertionResult.postValue(result)
        }
        Log.d("after insertEvent in viewmodel",dataInsertionResult.value.toString())
    }

    fun modifyEvent(eventId: String, event:Event) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("modifyEvent in viewmodel",event.placeName.toString())
        repository.modifyEvent(eventId, event){result ->
            _eventModificationResult.postValue(result)
        }
    }

    fun insertFilter(filter: Filter)= viewModelScope.launch(Dispatchers.IO)  {
        Log.d("insertFilter in viewmodel",filter.name.toString())
        Log.d("insertFilter in viewmodel",groupId.value.toString())
        repository.insertFilter(filter,groupId.value.toString()){result ->
            _filterInsertionResult.postValue(result)

        }
    }

    fun getMapIntentUri(coordinates: String, address: String): Uri {
        // Creates an Intent that will load a map of coordinates and pin of address
        return Uri.parse(coordinates + Uri.encode(address))
    }

    // fun delete event
    fun deleteEvent(event: Event) {
        repository.deleteEvent(event.eventId!!,groupId.value.toString(),filter.value?.id.toString())
    }
    // fun delete filter
    fun deleteFilter(filter: Filter) {
        repository.deleteFilter(filter, groupId.value.toString()){result ->
            _filterDeletionResult.postValue(result)
        }
    }

}

class ItineraryViewModelFactory(private val repository: ItineraryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ItineraryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItineraryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}