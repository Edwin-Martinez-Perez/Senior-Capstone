package com.example.collectivetrek

import android.R.id
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.collectivetrek.database.Event
import com.example.collectivetrek.database.Filter
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class ItineraryRepository {

    private val dbRef: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val filterRef = dbRef.getReference("Filters")
    private val userRef = dbRef.getReference("Users")
    private val eventRef = dbRef.getReference("Events")
    private val itineraryRef = dbRef.getReference("Itineraries")



    //filterId: String, groupId: String
    fun getFilteredEvents(groupId: String, filterId: String, callback: (Boolean) -> Unit): LiveData<List<Event>> {
        Log.d("getFilteredEvents",filterId)
        val eventsLiveData = MutableLiveData<List<Event>>()

        val filterReference = filterRef.child(groupId).child(filterId).child("events")
        val eventIdsList = mutableListOf<String>()

        filterReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                Log.d("getFilterEvents HERE", dataSnapshot.value.toString())
                for (eventIdSnapshot in dataSnapshot.children){
                    eventIdsList.add(eventIdSnapshot.key!!)
                }


                eventRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val eventsList = mutableListOf<Event>()
                        //Log.d("get filtered events Repository", dataSnapshot.children.joinToString())

                        for (eventSnapshot in dataSnapshot.children) {
                            val eventId = eventSnapshot.key

                            if (eventId in eventIdsList) {
                                val placeName = eventSnapshot.child("placeName").getValue(String::class.java)
                                val date = eventSnapshot.child("date").getValue(String::class.java)
                                val pinNum = eventSnapshot.child("pinNum").getValue(Int::class.java)
                                val coordinates = eventSnapshot.child("coordinates").getValue(String::class.java)
                                val address = eventSnapshot.child("address").getValue(String::class.java)
                                val note = eventSnapshot.child("note").getValue(String::class.java)
                                val bitmap = eventSnapshot.child("bitmap").getValue(String::class.java)

                                if (eventId != null) {
                                    val event = Event(eventId=eventId,placeName=placeName, date = date, pinNum = pinNum, coordinates = coordinates, address = address, note=note, bitmap=bitmap)
                                    eventsList.add(event)
                                }
                            }
                        }

                        Log.d("Itinerary Repository event is empty", eventsList.isNullOrEmpty().toString())
                        eventsLiveData.value = eventsList
                        if (eventIdsList.isNotEmpty()){
                            callback(true)
                        }
                        else{
                            callback(false)
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        // Handle errors here
                        callback(false)
                    }

                })
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
            }
        })



        return eventsLiveData
    }


    fun getFilters(groupId: String, callback: (Boolean) -> Unit): LiveData<List<Filter>> {
        val filtersLiveData = MutableLiveData<List<Filter>>()

        val filterReference = filterRef.child(groupId)
        Log.d("filter ref",filterReference.toString())

        filterReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                Log.d("filter reference", filterReference.toString())

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
                        filtersList.add(filter)
                    }
                }

                filtersLiveData.value = filtersList
                Log.d("getFilters livedata", filtersLiveData.value.toString())
                if (filtersList.isNotEmpty()){
                    callback(true)
                }
                else{
                    callback(false)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle errors here
                Log.d("onCancelled", databaseError.toString())
            }

        })



        Log.d("getFilters", "before return")

        return filtersLiveData
    }



    fun insertEvent(filterId: String, event: Event, groupId: String, callback: (Boolean) -> Unit) {
        Log.d("InsertEvent in Repo",filterId)
        Log.d("InsertEvent in Repo",groupId)
        //insert to firebase
        //insert
        val eventId = eventRef.push().key!! //unique event id
        Log.d("InsertEvent in Repo",eventRef.child(eventId).toString())
        eventRef.child(eventId).setValue(event)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful) {
                    filterRef.child(groupId).child(filterId).child("events").child(eventId).setValue(true)
                        .addOnCompleteListener{task ->
                            if (task.isSuccessful){
                                callback(true) // Callback indicating success
                            }
                        }
                } else {
                    callback(false) // Callback indicating failure
                }
                Log.d("Repository insert event","${event.placeName!!}")
            }.addOnFailureListener{err ->
                Log.d("Repository insert event",err.toString())
            }

        //filter id is the selected filter
        //filterRef.child(tempGroupId).child(filterId).child("events").child(eventId).setValue(true)
        //filterRef.child(groupId).child(filterId).child("events").child(eventId).setValue(true)
    }

    fun insertFilter(filter: Filter, groupId: String, callback: (Boolean) -> Unit) {
        //insert to firebase
        val filterId = filterRef.push().key!!
        filter.id = filterId

        //filterRef.child(tempGroupId).child(filterId).setValue(filter)
        filterRef.child(groupId).child(filterId).setValue(filter)
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    Log.d("insert event",filterRef.child(groupId).toString())
                    Log.d("Sucsess Repository insert event",filter.name!!)
                    callback(true) // Callback indicating success
                } else {
                    callback(false) // Callback indicating failure
                }
                Log.d("Repository insert event",filter.name!!)
            }.addOnFailureListener{err ->
                Log.d("Repository insert event",err.toString())
            }

    }

    fun modifyEvent(eventId: String, newEvent: Event, callback: (Boolean) -> Unit){
        eventRef.child(eventId).setValue(newEvent)
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    Log.d("Sucsess Repository modify event",newEvent.placeName.toString())
                    callback(true) // Callback indicating success
                } else {
                    callback(false) // Callback indicating failure
                }
                Log.d("Repository modify event",newEvent.date.toString())
            }.addOnFailureListener{err ->
                Log.d("Repository modify event",err.toString())
            }
    }

    fun deleteEvent(eventId: String, groupId:String, filterId: String) {
        //delete from firebase
        //from event
        //from filter : find which filter the event is in, and delete
        Log.d("deleteEvent", "${eventId+" "+groupId}")
        val eventQuery: Query = eventRef.child(eventId)
        val filterQuery: Query = filterRef.child(groupId).child(filterId).child("events")

        Log.d("event Query", eventQuery.ref.toString())
        Log.d("filter Query", filterQuery.ref.toString())

        eventQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (eventSnapshot in dataSnapshot.children) {
                    Log.d("deleting event", eventSnapshot.key.toString())
                    eventSnapshot.ref.removeValue()
                }
                filterQuery.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (filterSnapshot in dataSnapshot.children) {
                            Log.d("deleting filter", filterSnapshot.toString())

                            if (filterSnapshot.key == eventId){
                                Log.d("event found", filterSnapshot.key.toString())
                                Log.d("event found", filterSnapshot.ref.toString())
                                filterSnapshot.ref.removeValue()
                            }
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.d("TAG", "onCancelled", databaseError.toException())
                    }
                })

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", "onCancelled", databaseError.toException())
            }
        })

    }

    fun deleteFilter(filter: Filter, groupId:String, callback: (Boolean) -> Unit) {
        //delete filter from firebase
        val filterQuery: Query = filterRef.child(groupId).child(filter.id!!)
        var eventIds = mutableListOf<String>()

        filterQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (filterSnapshot in dataSnapshot.child("events").children) {
                    Log.d("deleting filter  1", filterSnapshot.key.toString())
//                    for (event in filterSnapshot.children){
//                        Log.d("event id", event.key.toString())
//                    }
                    eventIds.add(filterSnapshot.key.toString())
                }
                //filterSnapshot.ref.removeValue()
                Log.d("eventIds",eventIds.toString())
                //delete event first
                for (eventId in eventIds){
                    Log.d("eventId being deleted",eventIds.toString())
                    deleteEvent(eventId, groupId, filter.id!!)
                }

                //delete filter
                for (filterSnapshot in dataSnapshot.children) {
                    Log.d("deleting filter  2", filterSnapshot.ref.toString())
                    filterSnapshot.ref.removeValue()
                }

                val idRef: DatabaseReference = filterRef.child(groupId).child(filter.id!!)

                idRef.removeValue().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Data was successfully deleted
                        println("Data successfully deleted for ID: $filter.id")
                        Log.d("filter deletion", "before callback")
                        callback(true)
                    } else {
                        // An error occurred during the delete operation
                        println("Error deleting data for ID: $filter.id. ${task.exception?.message}")
                    }
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("TAG", "onCancelled", databaseError.toException())
                callback(false)
            }
        })
    }


}