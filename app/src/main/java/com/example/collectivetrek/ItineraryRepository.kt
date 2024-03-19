package com.example.collectivetrek

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.collectivetrek.database.Event
import com.example.collectivetrek.database.Filter
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ItineraryRepository {

    private val dbRef: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val filterRef = dbRef.getReference("Filters")
    private val userRef = dbRef.getReference("Users")
    private val eventRef = dbRef.getReference("Events")
    private val itineraryRef = dbRef.getReference("Itineraries")

    fun getFilteredEvents(filterId: String, groupId: String): LiveData<List<Event>> {
        val eventsLiveData = MutableLiveData<List<Event>>()
        //TODO make sure the db structure and db path
        val filterReference = filterRef.child(filterId).child("events")

        filterReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val eventsList = mutableListOf<Event>()

                for (eventSnapshot in dataSnapshot.children) {
                    val eventId = eventSnapshot.key
                    val date = eventSnapshot.child("date").getValue(String::class.java)
                    val pinNum = eventSnapshot.child("pinNum").getValue(Int::class.java)
                    val coordinates = eventSnapshot.child("coordinates").getValue(String::class.java)
                    val address = eventSnapshot.child("address").getValue(String::class.java)
                    /*
                    val placeName: String? = null,
                    val date: String? = null,
                    val pinNum: Int? = null,
                    val coordinates: String? = null,
                    val address: String? = null
                     */

                    if (eventId != null) {
                        val event = Event(eventId, date, pinNum, coordinates, address)
                        eventsList.add(event)
                    }
                }

                eventsLiveData.value = eventsList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })

        return eventsLiveData
    }

    fun getFilters(groupId: String): LiveData<List<Filter>> {
        val filtersLiveData = MutableLiveData<List<Filter>>()
        val filterReference = itineraryRef.child(groupId).child("filters")

        filterReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val filtersList = mutableListOf<Filter>()

                for (eventSnapshot in dataSnapshot.children) {
                    val filterId = eventSnapshot.key
                    val filterName = eventSnapshot.child("events").getValue(String::class.java)
                    /*
                    "Filters" : {
                        "filter id 1" : {
                            "events" : {
                            "event id 1" : True,
                            "event id 2" : True,
                            "event id 3" : True
                            }
                        },
                     */

                    if (filterId != null) {
                        val filter = Filter(filterName)
                        filtersList.add(filter)
                    }
                }

                filtersLiveData.value = filtersList
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })

        return filtersLiveData
    }

    fun insertEvent(event: Event) {
        //insert to firebase
        //insert
        val eventId = eventRef.push().key!! //unique event id
        eventRef.child(eventId).setValue(event)
            .addOnCompleteListener{
                Log.d("Repository insert event",event.placeName!!)
            }.addOnFailureListener{err ->
                Log.d("Repository insert event",err.toString())
            }
    }

    fun insertFilter(filter: Filter) {
        //insert to firebase
        val filterId = filterRef.push().key!!

        filterRef.child(filterId).setValue(filter)
            .addOnCompleteListener{
                Log.d("Repository insert event",filter.name!!)
            }.addOnFailureListener{err ->
                Log.d("Repository insert event",err.toString())
            }

    }

    fun deleteEvent(filter: Filter) {
        //delete from firebase
    }

    fun deleteFilter(filter: Filter) {
        //delete from firebase
    }


}