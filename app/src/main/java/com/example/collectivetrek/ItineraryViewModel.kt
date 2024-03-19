package com.example.collectivetrek

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.collectivetrek.database.Event
import com.example.collectivetrek.database.Filter
import com.google.firebase.Firebase
import com.google.firebase.database.database
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ItineraryViewModel(private val repository: ItineraryRepository): ViewModel() {
    // TODO
    // get group id from the group page before coming to itinerary page
    val groupid = "group1"

    val itinerarydb = Firebase.database.reference.child("Itinerary")
    val eventdb = Firebase.database.reference.child("Event")


    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> = _event

    private val _filter = MutableLiveData<Filter>()
    val filter: LiveData<Filter> = _filter

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
    }

    //get
    fun getEvent(): Event{
        return event.value!!
    }
    fun getFilter(): Filter{
        return filter.value!!
    }

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