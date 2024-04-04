package com.example.collectivetrek


import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat.startActivity
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.collectivetrek.database.Event
import com.example.collectivetrek.database.Filter

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ItineraryViewModel(private val repository: ItineraryRepository): ViewModel() {
    // TODO
    // get group id from the group page before coming to itinerary page

    //val groupid = "ABCDEFID2"
    val groupid = "ABCDEFID1"

    val groupid = "group1"

    val itinerarydb = Firebase.database.reference.child("Itinerary")
    val eventdb = Firebase.database.reference.child("Event")



    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> = _event

    private val _filter = MutableLiveData<Filter>()
    val filter: LiveData<Filter> = _filter


//    private val _filterId = MutableLiveData<String>()
//    val filterId: LiveData<String> = _filterId

//    private val _filterId = setFilterID()
//    val filterId: String = _filterId

    //live data for events
    //retrieve all the events from the group that user is currently in
    //TODO create function in repository to retrieve all the data

    val allEvents: LiveData<List<Event>> = repository.getFilteredEvents(groupid, filter.value?.id.toString()) {result->
        _filteredEventsShownResult.postValue(result)
    }

    var filters: LiveData<List<Filter>> = repository.getFilters(groupid){ result ->
        Log.d("filteres result", result.toString())
        _filterShownResult.postValue(result)
    }


    //var filteredEvents: LiveData<List<Event>> = repository.getFilteredEvents("groupid",filterId.toString())
    var filteredEvents: LiveData<List<Event>> = repository.getFilteredEvents(groupid,filter.value?.id.toString()){ result->
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

    //live data for events
    //retrieve all the events from the group that user is currently in
    //TODO create function in repository to retrieve all the data
    val allEvents: LiveData<List<Event>> = repository.getFilteredEvents("filterid",groupid)

    // TODO get filter id somehow
    val filteredEvents : LiveData<List<Event>> = repository.getFilteredEvents("filterid",groupid)

    val filteres: LiveData<List<Filter>> = repository.getFilters(groupid)




    //set event
    fun setEvent(event: Event){
        _event.value = event
    }
    fun setFilter(filter: Filter){
        _filter.value = filter

        Log.d("setFilter", filter.id.toString())
    }

    fun setFilterID(): String{
        return filter.value?.id.toString()
    }


    fun setFilters(groupId: String){
        Log.d("viewmodel","setFilters")
        filters = repository.getFilters(groupId){ result ->
            Log.d("viewmodel",result.toString())
            _filterShownResult.postValue(result)
        }
    }

    fun setFilteredEvents(){
        Log.d("set filter viewmodel",filter.value?.id.toString())
        filteredEvents = repository.getFilteredEvents(groupid,filter.value?.id.toString()){result->
            _filteredEventsShownResult.postValue(result)
        }
    }

    
    fun getEvent(): Event{
        return event.value!!
    }
    fun getFilter(): Filter{
        return filter.value!!
    }

    fun insertEvent(event: Event) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("insertEvent in viewmodel",filter.value?.id.toString())
        repository.insertEvent(filter.value?.id.toString(),event, groupid){ result ->
            _dataInsertionResult.postValue(result)
        }
    }

    fun modifyEvent(eventId: String, event:Event) = viewModelScope.launch(Dispatchers.IO) {
        Log.d("insertEvent in viewmodel",filter.value?.id.toString())
        //repository.modifyEvent()
    }

    fun insertFilter(filter: Filter)= viewModelScope.launch(Dispatchers.IO)  {
        Log.d("insertFilter in viewmodel",filter.name.toString())
        repository.insertFilter(filter,groupid){result ->
            _filterInsertionResult.postValue(result)

        }
    }

    fun getMapIntentUri(coordinates: String, address: String): Uri {
        // Creates an Intent that will load a map of coordinates and pin of address
        return Uri.parse(coordinates + Uri.encode(address))

    fun insertEvent(event: Event) {
        repository.insertEvent(event)
    }

    fun insertFilter(filter: Filter) {
        repository.insertFilter(filter)

    }

    // TODO let user add, modify note later
    fun addNote(){

    }

    // fun delete event
    fun deleteEvent() {
    }
    // fun delete filter
    fun deleteFilter() {
    }


//    fun updateFilteredEvents() {
//        viewModelScope.launch {
//            val events = repository.getFilteredEvents()
//            _filteredEvents.postValue(events.value)
//            Log.d("filteredEvents", _filteredEvents.value.toString())
//        }
//    }

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