package com.example.collectivetrek.fragments

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.app.AlertDialog
import android.app.ProgressDialog.show
import android.content.DialogInterface
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.collectivetrek.EventAdapter
import com.example.collectivetrek.EventAdapterDeleteCallback
import com.example.collectivetrek.EventAdapterCallback
import com.example.collectivetrek.EventItineraryListener
import com.example.collectivetrek.FilterAdapter
import com.example.collectivetrek.FilterItineraryListener
import com.example.collectivetrek.ItineraryRepository
import com.example.collectivetrek.ItineraryViewModel
import com.example.collectivetrek.ItineraryViewModelFactory
import com.example.collectivetrek.R
import com.example.collectivetrek.database.Event
import com.example.collectivetrek.database.Filter
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.example.collectivetrek.databinding.FragmentItineraryBinding
import com.google.android.material.textfield.TextInputLayout
import java.util.Calendar
import java.util.Date

/*
‼️クリック時フィルターの色を変える (別のフィルターがクリックされたらそのフィルターを戻す)
‼️Googleマップに飛ぶときに、パーミッションを取る (https://www.geeksforgeeks.org/how-to-get-current-location-in-android/)
‼️datepickerの、dateを4/31とかないものを表示しないようにする
‼️一番最初のデフォルトフィルターを作る！！
(Friday)add eventのaddressを入力するときに、検索候補を表示する
‼️エラーzzf - shutdown(),
☑️eventsがない時のイラストを変える(サイズ、テキストも追加する)
☑️fabの形を変える (Theme)
☑️filterを消すボタンを作る(filterをaddするボタンの代わりに、編集ボタンを作って、addとdeleteができるようにする)
☑️filterを消すときに、もしeventがそのfilterにあったら、ほんとに消していいか聞く
☑️filter delete
☑️filter delete dialog
☑️delete menuを作る
☑️2回目以降の画像が保存されない問題() -> viewmodel no results たちを、使い終わったら元に戻す
☑️x y の値を変える bitmap
☑️event cardviewのマックスサイズを指定する
☑️event cardview: 画像をラウンド&&正方形にする、マージンをたす、noteとaddressの隙間をなくす
☑️event cardview: サイズを内容に合わせて変える
☑️画像をイベントに追加する
☑️(Friday)safe args 送れるようにする
☑️eventをスクローラブルにする
☑️(Friday)フィルターがクリックされた時&アプリ起動時に、eventsがロードしない時がある。callbackがfalseを返す。それを直す。
☑️Toastのデザインを変える
 */


class ItineraryFragment : Fragment(), EventAdapterCallback, EventAdapterDeleteCallback {

    private var _binding: FragmentItineraryBinding? = null
    private val binding get() = _binding!!


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
            eventsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.itineraryFragment = this

        Log.d("filter id", itineraryViewModel.filter.value?.id.toString())

        val eventAdapter = EventAdapter(EventItineraryListener { event ->
            itineraryViewModel.setEvent(event)

            // TODO get event id somehow
            val eventId = event.eventId!!
            //val eventId = "-NuUsqrTkBUKVui5XSmg"

            //TODO makes a event editable
            showEditEventDialog(eventId)
            Log.d("DictionaryHome word obj","filter")
        },this, this)
        binding.eventsRecyclerView.adapter = eventAdapter

        val filterAdapter = FilterAdapter(FilterItineraryListener { filter ->
            Log.d("Itinerary Fragment","filterAdapter")

            // when filter clicked, show the events with that filter
            itineraryViewModel.setFilter(filter)
            Log.d("Filter",itineraryViewModel.filter.value?.id.toString())
            itineraryViewModel.setFilteredEvents()

            //change the filter name
            binding.filterName.setText(filter.name)

            itineraryViewModel.filteredEvents.observe(viewLifecycleOwner) { events ->
                if (events.isEmpty()){
                    // TODO Change illustration on the screen and say "create an event!" or something
                    // Show image when there is no event available under the filter
                    binding.itineraryImage.visibility = View.VISIBLE
                    Log.d("Tag", "event is empty")
                } else {
                    // Remove image when there is an event available under the filter
                    binding.itineraryImage.visibility = View.GONE
                    Log.d("Tag", "Number of events: ${events.size}")
                    events.let { eventAdapter.submitList(it) }
                }
            }
            itineraryViewModel.filteredEvents.value.let { eventAdapter.submitList(it) }
        })
        binding.itineraryFilterRecycler.adapter = filterAdapter




        //val groupId = "ABCDEFID3"
        val groupId = "ABCDEFID4"
        //itineraryViewModel.setFilters(groupId = "ABCDEFID1") //TODO change to groupid
        //itineraryViewModel.setFilters(groupId = "ABCDEFID2") //TODO change to groupid
        itineraryViewModel.setFilters(groupId = groupId) //TODO change to groupid

        // show list of filtered events
        itineraryViewModel.filters.observe(viewLifecycleOwner) { filters ->
            Log.d("Tag", "Number of filters: ${filters.size}")

            if (filters.isEmpty()){
                // TODO create filter each for each date
                // TODO 1 when user created the group with dates for the first time, create filters based on the dates
                val filter = Filter(name = "Add Filter")
                val placeHolderFilter = mutableListOf<Filter>()
                placeHolderFilter.add(filter)

                placeHolderFilter.let { filterAdapter.submitList(it) }

                binding.itineraryImage.visibility = View.VISIBLE

            } else {
                filters.let { filterAdapter.submitList(it) }
            }
        }

        itineraryViewModel.filterShownResult.observe(viewLifecycleOwner){ result ->
            if (result){
                // filter shown success
                Log.d("callback","filter shown success")
                Log.d("filtered events null",(itineraryViewModel.filteredEvents.value.isNullOrEmpty()).toString())
                if (itineraryViewModel.filter.value?.id.isNullOrEmpty() && itineraryViewModel.filters.value!!.isNotEmpty()){ //TODO check this condition
                    // show the events in the first filter when there is no filter selected
                    itineraryViewModel.setFilter(itineraryViewModel.filters.value!![0])
                    binding.filterName.setText(itineraryViewModel.filters.value!![0].name)
                }else{
                    binding.filterName.setText(itineraryViewModel.filter.value!!.name)
                }
                itineraryViewModel.setFilteredEvents()

                //make FAB visible since filter is available
                binding.addEventButton.visibility = View.VISIBLE

                itineraryViewModel.filteredEventsShownResult.observe(viewLifecycleOwner){result->
                    if(result){
                        Log.d("result",result.toString())

                        if (!itineraryViewModel.filteredEvents.value.isNullOrEmpty()){
                            // show background image when there is no events under the filter
                            binding.itineraryImage.visibility = View.GONE
                        }
                        itineraryViewModel.filteredEvents.value.let { eventAdapter.submitList(it) }
                    } else{
                        Log.d("result false", result.toString())
                        if (!itineraryViewModel.filters.value.isNullOrEmpty() && itineraryViewModel.filters.value!!.isNotEmpty()){
                            // show fab when filter is shown
                            //Log.d("filters show FAB",itineraryViewModel.filters.value.toString())
                            binding.addEventButton.visibility = View.VISIBLE
                        }
                        if (itineraryViewModel.filteredEvents.value.isNullOrEmpty()){
                            // show background image when there is no events under the filter
                            Log.d("filtered events",itineraryViewModel.filteredEvents.value.toString())
                            binding.itineraryImage.visibility = View.VISIBLE
                        }
                    }
                }
            } else{//filter is empty (no filter is added)
                //
                Log.d("filter result false", result.toString())
                binding.itineraryImage.visibility = View.VISIBLE
                binding.addEventButton.visibility = View.GONE
            }
        }



        //buttons
        binding.addEventButton.setOnClickListener {
             //go to add event fragment
            //findNavController().navigate(R.id.action_itineraryFragment_to_addEventFragment)

            val action = ItineraryFragmentDirections.actionItineraryFragmentToAddEventFragment("ABCDEFID1")


//            val action = ItineraryFragmentDirections.actionItineraryFragmentToAddEventFragment(groupId = "ABCDEFID1")
            findNavController().navigate(action)
        }

        binding.addFilterButton.setOnClickListener {
            // go to add filter fragment
            findNavController().navigate(R.id.action_itineraryFragment_to_addFilterFragment)
        }

        binding.deleteFilter.setOnClickListener {

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Deleting ${itineraryViewModel.getFilter().name}")
                .setMessage("Are you sure deleting a ${itineraryViewModel.getFilter().name}?\n" +
                        "This can delete all the events in ${itineraryViewModel.getFilter().name}.")
                .setNeutralButton("Cancel") { dialog, which ->
                    // Respond to neutral button press
                }
                .setPositiveButton("Delete") { dialog, which ->
                    // Respond to positive button press
                    // delete Filter and all the events under that
                    itineraryViewModel.deleteFilter(itineraryViewModel.getFilter())

                    //TODO refresh filters
                    itineraryViewModel.filterDeletionResult.observe(viewLifecycleOwner){result->
                        if(result){
                            Log.d("filter deletion result", result.toString())
                            itineraryViewModel.setFilters(groupId)
                            itineraryViewModel.filters.observe(viewLifecycleOwner) { filters ->
                                if (filters.isEmpty()){
                                    val filter = Filter(name = "Add Filter")
                                    val placeHolderFilter = mutableListOf<Filter>()
                                    placeHolderFilter.add(filter)

                                    placeHolderFilter.let { filterAdapter.submitList(it) }

                                    binding.itineraryImage.visibility = View.VISIBLE

                                } else {
                                    itineraryViewModel.setFilter(filters[0])
                                    filters.let { filterAdapter.submitList(it) }
                                }
                            }
                        }
                    }
                }
                .show()
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

    }



    private fun showEditEventDialog(eventId: String){
        val builder = AlertDialog.Builder(requireContext())
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.edit_event_dialog, null)
        val place = dialogLayout.findViewById<TextInputLayout>(R.id.editEvent_place_editText)
        val address = dialogLayout.findViewById<TextInputLayout>(R.id.editEvent_address_editText)
        val dateLayout = dialogLayout.findViewById<TextInputLayout>(R.id.editEvent_date_TextInput)
        val dateEditText = dialogLayout.findViewById<TextInputEditText>(R.id.editEvent_date_EditText)
        val note = dialogLayout.findViewById<TextInputLayout>(R.id.editEvent_note_editText)

        place.editText?.setText(itineraryViewModel.event.value?.placeName.toString())
        address.editText?.setText(itineraryViewModel.event.value?.address.toString())
        dateLayout.editText?.setText(itineraryViewModel.event.value?.date.toString())
        note.editText?.setText(itineraryViewModel.event.value?.note.toString())

        dateEditText.setOnClickListener{
            Log.d("date clicked","showdate")
            showDate(dateLayout)
        }

        with(builder){
            setPositiveButton("Done"){ dialog, which ->

                val newEvent = Event(
                    placeName = place.editText?.text.toString(),
                    address = address.editText?.text.toString(),
                    date = dateLayout.editText?.text.toString(),
                    note = note.editText?.text.toString(),
                    bitmap = itineraryViewModel.event.value?.bitmap)
                itineraryViewModel.modifyEvent(eventId, newEvent)
                itineraryViewModel.eventModificationResult.observe(viewLifecycleOwner){result->
                    if(result){
                        Log.d("modification done", itineraryViewModel.event.toString())
                        Toast.makeText(context, "Event saved.", Toast.LENGTH_LONG).show()
                    }
                }

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

    override fun onDeleteEventClick(clickedEvent:Event, deleteButton: ImageButton){
        Log.d("onDeleteClick", clickedEvent.placeName.toString())

        val popupMenu = PopupMenu(requireContext(), deleteButton)

        // Inflate the menu from XML
        popupMenu.menuInflater.inflate(R.menu.delete_menu, popupMenu.menu)

        // Set a click listener for menu items
        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.delete_event_button -> {
                    // Handle Popup Item 1 click

                    //delete event from database
                    itineraryViewModel.deleteEvent(clickedEvent)

                    //reload the events after making sure the item is deleted
                    itineraryViewModel.setFilteredEvents()
                    true
                }
                R.id.cancel_event_button -> {
                    // Handle Popup Item 2 click
                    true
                }
                else -> false
            }
        }

        // Show the popup menu
        popupMenu.show()

    }

    private fun showDate(date: TextInputLayout) {
        Log.d("showData executed","showdate")
        // Set up the MaterialDatePicker
        val builder = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Select Date")
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setTheme(R.style.Theme_App_DatePicker)

        // Create the MaterialDatePicker
        val materialDatePicker = builder.build()

        // Add a listener to handle date selection
        materialDatePicker.addOnPositiveButtonClickListener { selection ->
            val selectedDate = Date(selection)
            val calendar = Calendar.getInstance()
            calendar.time = selectedDate
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH) + 1
            val day = calendar.get(Calendar.DAY_OF_MONTH) + 1

            val formattedDate = "$month/$day/$year"
            date.editText?.setText(formattedDate)
        }

        // Show the MaterialDatePicker
        materialDatePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")
        // TODO change the color of positive and negative button
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}