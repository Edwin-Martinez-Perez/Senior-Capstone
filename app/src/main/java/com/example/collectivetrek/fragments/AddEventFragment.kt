package com.example.collectivetrek.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Constraints
import androidx.core.content.ContextCompat
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment

import com.example.collectivetrek.ItineraryRepository
import com.example.collectivetrek.ItineraryViewModel
import com.example.collectivetrek.ItineraryViewModelFactory

import com.example.collectivetrek.ItineraryViewModel

import com.example.collectivetrek.R
import com.example.collectivetrek.database.Event
import com.example.collectivetrek.databinding.FragmentAddEventBinding
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.*
import kotlin.reflect.typeOf


class AddEventFragment : Fragment() {
    private var _binding: FragmentAddEventBinding? = null
    private val binding get() = _binding!!


    private val itineraryViewModel: ItineraryViewModel by activityViewModels() {
        ItineraryViewModelFactory(repository = ItineraryRepository())
    }

    private val itineraryViewModel: ItineraryViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddEventBinding.inflate(inflater, container, false) //? attachToRoot false

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = itineraryViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = NavHostFragment.findNavController(this@AddEventFragment)
        Log.d("on view created","on view created")

        // TODO
        // add text watcher to place name and user leaves w/o any input, error

        //TODO
        binding.addEventDateEditText.setOnClickListener {
            Log.d("add event fragment","clicklistner")
            //showDate()
            showDate2()
        }

        // TODO
        binding.addEventAddButton.setOnClickListener {
            // retrieve filter from edit text and validate
            val placeName = binding.addEventPlaceEditText.editText?.text.toString()
            val address = binding.addEventAddressEditText.editText?.text.toString()
            var date = binding.addEventDateTextInput.editText?.text.toString()
            val note = binding.addEventNoteEditText.editText?.text.toString()
            Log.d("add event date", date)
            val event = Event(placeName,address=address, date = date, note = note)
            // validation
            if (checkEventFields(event)){
                // store in database
                addEventToDataBase(event)


                itineraryViewModel.dataInsertionResult.observe(viewLifecycleOwner){ result ->
                    if (result){
                        // make Toast
                        Toast.makeText(context, "Event added.", Toast.LENGTH_LONG).show()
                        Log.d("add event fragment",itineraryViewModel.filter.value?.id.toString())
                        // go back to itinerary page
                        navController.popBackStack()
                    }
                    else{
                        Toast.makeText(context, "Failed.", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.addEventCancelButton.setOnClickListener {
            // make Toast
            // go back to itinerary page
            Log.d("add filter","cancel clicked")
            //findNavController().popBackStack()
            navController.popBackStack()
        }
    }

    fun checkEventFields(event:Event) : Boolean {
        if (event.placeName!!.isEmpty()) {
            binding.addEventPlaceEditText.error = "Place name required."
            return false
        }
        else if (event.placeName.length > 200) {
            binding.addEventPlaceEditText.error = "Too long."
            return false
        }

        if (event.address != null){
            if (event.address!!.length > 400) {
                binding.addEventPlaceEditText.error = "Too long."
                return false
            }
        }


        if (event.date!!.isNotEmpty()) {
             if (event.date!!.length != 10 && event.date!!.length != 9 && event.date!!.length != 8) {
                // make Toast
                Toast.makeText(context, "Event added.", Toast.LENGTH_LONG).show()
                // go back to itinerary page
                Log.d("add filter","add clicked")
                navController.popBackStack()
            }
        }

        binding.addEventCancelButton.setOnClickListener {
            // make Toast
            // go back to itinerary page
            Log.d("add filter","cancel clicked")
            //findNavController().popBackStack()
            navController.popBackStack()
        }
    }

    fun checkEventFields(event:Event) : Boolean {
        if (event.placeName!!.isEmpty()) {
            binding.addEventPlaceEditText.error = "Place name required."
            return false
        }
        else if (event.placeName.length > 200) {
            binding.addEventPlaceEditText.error = "Too long."
            return false
        }

        if (event.address != null){
            if (event.address!!.length > 400) {
                binding.addEventPlaceEditText.error = "Too long."
                return false
            }
        }


        if (event.date!!.isNotEmpty()) {
             if (event.date!!.length != 10 || event.date!!.length != 9) {
                binding.addEventDateTextInput.error = "Invalid date length."
                return false
             }
        } //TODO add else if to validate the date is during the trip

        if (event.note != null) {
            if (event.note.length > 100000) {
                binding.addEventDateTextInput.error = "Too long."
                return false
            }
        }

        return true
    }

    private fun addEventToDataBase(event:Event) {

        Log.d("Add event to database", "called")

        itineraryViewModel.insertEvent(event)
    }

    //TODO show a month of trip starting date
    private fun showDate() {
        val calendar = Calendar.getInstance()

        val yyyy = calendar.get(Calendar.YEAR)
        val mm = calendar.get(Calendar.MONTH)
        val dd = calendar.get(Calendar.DAY_OF_MONTH)


        // on below line we are creating a
        // variable for date picker dialog.
        val datePickerDialog = DatePickerDialog(
            // on below line we are passing context.
            //R.style.DatePickerDialogTheme,
            requireContext(), { _, year, monthOfYear, dayOfMonth ->
                val date = ((monthOfYear + 1).toString() + "/" + dayOfMonth.toString() + "/" + year)
                binding.addEventDateTextInput.editText?.setText(date)
            },
            yyyy,
            mm,
            dd
        )
        datePickerDialog.show()
        datePickerDialog.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        datePickerDialog.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
        // TODO focus on the month of trip
        // use Constraints
    }

    // TODO add constraints, startdate, enddate, valid date
    // TODO change theme
    private fun showDate2() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
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
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val formattedDate = "$month/$day/$year"
            binding.addEventDateTextInput.editText?.setText(formattedDate)
        }

        // Show the MaterialDatePicker
        materialDatePicker.show(requireActivity().supportFragmentManager, "DATE_PICKER")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}