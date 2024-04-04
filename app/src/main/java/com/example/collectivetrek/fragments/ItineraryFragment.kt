package com.example.collectivetrek.fragments

import android.app.AlertDialog
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collectivetrek.EventAdapterCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collectivetrek.EventAdapter
import com.example.collectivetrek.EventItineraryListener
import com.example.collectivetrek.ItineraryViewModel
import com.example.collectivetrek.R
import com.example.collectivetrek.databinding.FragmentItineraryBinding
import com.example.collectivetrek.FilterAdapter
import com.example.collectivetrek.FilterItineraryListener
import com.example.collectivetrek.ItineraryRepository
import com.example.collectivetrek.ItineraryViewModelFactory
import com.example.collectivetrek.database.Event
import com.example.collectivetrek.database.Filter
import com.google.android.material.textfield.TextInputLayout

/*
‼️クリック時フィルターの色を変える
イベントの数字をなくしてそのかわりドットを入れる
‼️Googleマップに飛ぶときに、パーミッションを取る
itineraryが空の時に画像を追加する
Toastのデザインを変える
fabの形を変える (Theme)
‼️eventがclickされた時、eventの中身を編集できるようにする。date address, event name, note
‼️eventが編集された時、databaseを書き換える
‼️書き換えられたeventを表示する
filterを消すボタンを作る(filterをaddするボタンの代わりに、編集ボタンを作って、addとdeleteができるようにする)
filterを消すときに、もしeventがそのfilterにあったら、ほんとに消していいか聞く
 */

//TODO initializer
//TODO when user open the itinerary for the first time (with the specific group id, show the first page),
class ItineraryFragment : Fragment(), EventAdapterCallback {

class ItineraryFragment : Fragment() {

    private var _binding: FragmentItineraryBinding? = null
    private val binding get() = _binding!!
    // private val itineraryViewModel: ItineraryViewModel by activityViewModels()

    private val itineraryViewModel: ItineraryViewModel by activityViewModels {
        ItineraryViewModelFactory(repository = ItineraryRepository())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentItineraryBinding.inflate(inflater, container, false) //? attachToRoot false

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = itineraryViewModel

            itineraryFilterRecycler.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL, false)

            itineraryFilterRecycler.layoutManager = LinearLayoutManager(requireContext())
            eventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.itineraryFragment = this

        val eventAdapter = EventAdapter(EventItineraryListener { event ->
            itineraryViewModel.setEvent(event)

            // TODO get event id somehow
            val eventId = "event.id"

            //TODO makes a event editable
            showEditEventDialog(eventId)
            Log.d("DictionaryHome word obj","filter")
        },this)
        binding.eventsRecyclerView.adapter = eventAdapter

        val filterAdapter = FilterAdapter(FilterItineraryListener { filter ->
            Log.d("Itinerary Fragment","filterAdapter")

            // when filter clicked, show the events with that filter
            itineraryViewModel.setFilter(filter)
            Log.d("Filter",itineraryViewModel.filter.value?.id.toString())
            itineraryViewModel.setFilteredEvents()

            itineraryViewModel.filteredEvents.observe(viewLifecycleOwner) { events ->
                if (events.isEmpty()){
                    // TODO Change to put illustration on the screen and say "create an event!" or something
                    val event = Event(placeName = "Sample Place")
                    val placeHolderEvent = mutableListOf<Event>()
                    placeHolderEvent.add(event)

                    placeHolderEvent.let { eventAdapter.submitList(it) }
                    Log.d("Tag", "event is empty")
                } else {
                    Log.d("Tag", "Number of events: ${events.size}")
                    events.let { eventAdapter.submitList(it) }
                }
            }
            itineraryViewModel.filteredEvents.value.let { eventAdapter.submitList(it) }
        })
        binding.itineraryFilterRecycler.adapter = filterAdapter




        itineraryViewModel.setFilters(groupId = "ABCDEFID1") //TODO change to groupid
        //itineraryViewModel.setFilters(groupId = "ABCDEFID2") //TODO change to groupid

        // show list of filtered events
        itineraryViewModel.filters.observe(viewLifecycleOwner) { filters ->
            Log.d("Tag", "Number of filters: ${filters.size}")

            if (filters.isEmpty()){
                // TODO create filter each for each date
                // TODO 1 when user created the group with dates for the first time, create filters based on the dates
                val filter = Filter(name = "Sample")
                val placeHolderFilter = mutableListOf<Filter>()
                placeHolderFilter.add(filter)

                placeHolderFilter.let { filterAdapter.submitList(it) }

            } else {
                filters.let { filterAdapter.submitList(it) }
            }
        }

        itineraryViewModel.filterShownResult.observe(viewLifecycleOwner){ result ->
            if (result){
                // filter shown success
                Log.d("callback","filter shown success")
                if (itineraryViewModel.filter.value?.id.isNullOrEmpty()){
                    // show the events in the first filter when there is no filter selected
                    itineraryViewModel.setFilter(itineraryViewModel.filters.value!![0])
                }
                itineraryViewModel.setFilteredEvents()

                itineraryViewModel.filteredEventsShownResult.observe(viewLifecycleOwner){result->
                    if(result){
                        Log.d("under call back", itineraryViewModel.filteredEvents.value.toString())
                        itineraryViewModel.filteredEvents.value.let { eventAdapter.submitList(it) }
                    }
                }
            }
        }
        val eventAdapter = EventAdapter(EventItineraryListener { event ->
            itineraryViewModel.setEvent(event)
            Log.d("DictionaryHome word obj","filter")
        })

//        binding?.dictionaryHomeFragment = this
//        val adapter = DictionaryHomeAdapter(DictionaryHomeListener { word ->
//            sharedViewModel.setWord(word)
//            Log.d("DictionaryHome word obj",word.imageFileName.toString())
//            //navigate to definition
//            findNavController().navigate(R.id.action_dictionaryHomeFragment_to_wordDefinitionFragment)
//        })
//        binding.recyclerView.adapter = adapter

        val filterAdapter = FilterAdapter(FilterItineraryListener { filter ->
            itineraryViewModel.setFilter(filter)
            Log.d("DictionaryHome word obj","filter")
            // TODO
            // when filter clicked, show the events with that filter
        })

        binding.itineraryFilterRecycler.adapter = filterAdapter
        binding.eventsRecyclerView.adapter = eventAdapter

        // TODO 1 when user created the group with dates for the first time, create filters based on the dates
        // check db if theres any event, filter, if not,
        // show the sample event in all and each filter

        // show list of all events
        itineraryViewModel.allEvents.observe(viewLifecycleOwner) { events ->
            // Update the cached copy of the words in the adapter.
            Log.d("Tag", "Number of events: ${events.size}")
            events.let { eventAdapter.submitList(it) }
        }



        // show list of filtered events
        itineraryViewModel.filteres.observe(viewLifecycleOwner) { filters ->
            Log.d("Tag", "Number of events: ${filters.size}")
            filters.let { filterAdapter.submitList(it) }
        }
        //buttons
        binding.addEventButton.setOnClickListener {
            // go to add event fragment
            findNavController().navigate(R.id.action_itineraryFragment_to_addEventFragment)
        }

        binding.addFilterButton.setOnClickListener {
            // go to add filter fragment
            findNavController().navigate(R.id.action_itineraryFragment_to_addFilterFragment)
        }
    }

    fun openMap(eventAddress: String){

        // TODO before open the mapp automatically, ask user for permission
        val coder = Geocoder(requireContext())
        var address = coder.getFromLocationName(eventAddress, 1)
        Log.d("address is empty",address!!.isEmpty().toString())
        if (address!!.isNotEmpty()){
            val lat = address[0].latitude
            val long = address[0].longitude

            val gmmIntentUri = itineraryViewModel.getMapIntentUri("geo:${lat}, ${long}?q=",eventAddress)
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            startActivity(mapIntent)
        } else{
            Toast.makeText(context, "Invalid Address. Failed to open map.", Toast.LENGTH_LONG).show()
        }
        Log.d("openMap",address.toString())
        // TODO : make try and exception


//        val gmmIntentUri = itineraryViewModel.getMapIntentUri("geo:41.77280699810813, -88.14363869347696?q=","30 N Brainard St, Naperville, IL 60540")
//        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//        mapIntent.setPackage("com.google.android.apps.maps")
//        startActivity(mapIntent)
    }

    fun showEditEventDialog(eventId: String){

        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_event_dialog, null)
        val place = dialogLayout.findViewById<TextInputLayout>(R.id.editEvent_place_editText)
        val address = dialogLayout.findViewById<TextInputLayout>(R.id.editEvent_address_editText)
        val date = dialogLayout.findViewById<TextInputLayout>(R.id.editEvent_date_TextInput)

        with(builder){
            setPositiveButton("Done"){ dialog, which ->
                //TODO modify database
                Log.d("dialog",place.editText?.text.toString())
                Log.d("dialog",address.editText?.text.toString())
                Log.d("dialog",date.editText?.text.toString())
                //itineraryViewModel.modifyEvent(eventId,Event(address = address.editText?.text.toString(),
                    //placeName = place.editText?.text.toString(), date = date.editText?.text.toString()))

                // TODO show the updated events
                itineraryViewModel.setFilteredEvents()
            }
            setNegativeButton("Cancel"){dialog, which ->
                Log.d("dialog cancel", "cancel clicked")
            }
            setView(dialogLayout)
            show()
        }
    }

    override fun onAddressClick(address: String) {
        // When address textview clicked, execute openMap
        Log.d("onAddressClick",address)
        openMap(address)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}