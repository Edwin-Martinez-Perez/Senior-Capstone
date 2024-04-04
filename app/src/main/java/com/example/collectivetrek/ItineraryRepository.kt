package com.example.collectivetrek

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.collectivetrek.database.Event
import com.example.collectivetrek.database.Filter
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class ItineraryRepository {

    private val dbRef: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val filterRef = dbRef.getReference("Filters")
    private val userRef = dbRef.getReference("Users")
    private val eventRef = dbRef.getReference("Events")
    private val itineraryRef = dbRef.getReference("Itineraries")



//    fun getAllEvents(groupId: String): LiveData<List<Event>> {
//        val allEventsLiveData = MutableLiveData<List<Event>>()
//        val eventReference = filterRef.child(tempGroupId)
//        Log.d("all Events Repository", allEventsLiveData.toString())
//        val filters = getFilters(tempGroupId){ result ->
//            _eventShownResult.postValue(result)
//        }
//
//
//        return allEventsLiveData
//    }


    //filterId: String, groupId: String
    fun getFilteredEvents(groupId: String, filterId: String, callback: (Boolean) -> Unit): LiveData<List<Event>> {
        Log.d("getFilteredEvents",filterId)
        val eventsLiveData = MutableLiveData<List<Event>>()

        //TODO make sure the db structure and db path
        //val filterReference = filterRef.child(tempGroupId).child(filterId).child("events")
        val filterReference = filterRef.child(groupId).child(filterId).child("events")
        val eventIdsList = mutableListOf<String>()

        filterReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                Log.d("getFilterEvents HERE", dataSnapshot.value.toString())
                for (eventIdSnapshot in dataSnapshot.children){
                    eventIdsList.add(eventIdSnapshot.key!!)
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })


        eventRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val eventsList = mutableListOf<Event>()
                Log.d("get filtered events Repository", dataSnapshot.children.joinToString())

                for (eventSnapshot in dataSnapshot.children) {
                    val eventId = eventSnapshot.key

                    if (eventId in eventIdsList) {
                        val placeName = eventSnapshot.child("placeName").getValue(String::class.java)
                        val date = eventSnapshot.child("date").getValue(String::class.java)
                        val pinNum = eventSnapshot.child("pinNum").getValue(Int::class.java)
                        val coordinates = eventSnapshot.child("coordinates").getValue(String::class.java)
                        val address = eventSnapshot.child("address").getValue(String::class.java)
                        val note = eventSnapshot.child("note").getValue(String::class.java)

                        if (eventId != null) {
                            val event = Event(placeName, date, pinNum, coordinates, address, note=note)
                            eventsList.add(event)
                        }
                    }
                }

                Log.d("Itinerary Repository event", eventsList.toString())
                eventsLiveData.value = eventsList
                callback(true)
=======
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

    fun getFilters(groupId: String, callback: (Boolean) -> Unit): LiveData<List<Filter>> {
//        Log.d("getFilters",tempGroupId)
        Log.d("getFilters",groupId)
        val filtersLiveData = MutableLiveData<List<Filter>>()
        //val filterReference = filterRef.child(tempGroupId)
        val filterReference = filterRef.child(groupId)
        Log.d("filter ref",filterReference.toString())

        filterReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val filtersList = mutableListOf<Filter>()

                for (eventsSnapshot in dataSnapshot.children) {
                    val filterId = eventsSnapshot.key
                    val filterName = eventsSnapshot.child("name").getValue(String::class.java)
                    val filterEvents = mutableListOf<String>()
                    for (eventSnapshot in eventsSnapshot.child("events").children){
                        val event = eventSnapshot.key
                        filterEvents.add(event!!)
                    }

                    if (filterId != null) {
                        val filter = Filter(filterName,filterId,filterEvents)

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
                Log.d("getFilters livedata", filtersLiveData.value.toString())
                if (filtersLiveData != null){
                    callback(true)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here

                Log.d("onCancelled", databaseError.toString())
            }

        })
        Log.d("getFilters", "before return")

            }
        })


        return filtersLiveData
    }




    fun insertEvent(filterId: String, event: Event, groupId: String, callback: (Boolean) -> Unit) {
        Log.d("InsertEvent in Repo",filterId)

    fun insertEvent(event: Event) {

        //insert to firebase
        //insert
        val eventId = eventRef.push().key!! //unique event id
        eventRef.child(eventId).setValue(event)

            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    callback(true) // Callback indicating success
                } else {
                    callback(false) // Callback indicating failure
                }

            .addOnCompleteListener{

                Log.d("Repository insert event",event.placeName!!)
            }.addOnFailureListener{err ->
                Log.d("Repository insert event",err.toString())
            }


        //TODO filter id is the selected filter
        //filterRef.child(tempGroupId).child(filterId).child("events").child(eventId).setValue(true)
        filterRef.child(groupId).child(filterId).child("events").child(eventId).setValue(true)
    }

    fun insertFilter(filter: Filter, groupId: String, callback: (Boolean) -> Unit) {
        //insert to firebase
        val filterId = filterRef.push().key!!

        // TODO groupid should be shared across the pages within one group
        //filterRef.child(tempGroupId).child(filterId).setValue(filter)
        filterRef.child(groupId).child(filterId).setValue(filter)
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    Log.d("Sucsess Repository insert event",filter.name!!)
                    callback(true) // Callback indicating success
                } else {
                    callback(false) // Callback indicating failure
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