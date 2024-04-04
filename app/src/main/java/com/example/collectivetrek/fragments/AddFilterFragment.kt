package com.example.collectivetrek.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.collectivetrek.ItineraryRepository
import com.example.collectivetrek.ItineraryViewModel
import com.example.collectivetrek.ItineraryViewModelFactory
import com.example.collectivetrek.ItineraryViewModel
import com.example.collectivetrek.R
import com.example.collectivetrek.database.Filter
import com.example.collectivetrek.databinding.FragmentAddFilterBinding


class AddFilterFragment : Fragment() {
    private var _binding: FragmentAddFilterBinding? = null
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
        _binding = FragmentAddFilterBinding.inflate(inflater, container, false) //? attachToRoot false

        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            viewModel = itineraryViewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = NavHostFragment.findNavController(this@AddFilterFragment)


        binding.addFilterAddButton.setOnClickListener {
            // retrieve filter from edit text and validate
            val filterName = binding.addFilterFilterEditText.editText?.text.toString()
            Log.d("add filter fragment",filterName)
            if (checkFilterName(filterName)) {
                // store in database

                val filter = Filter(name=filterName)
                addFilterToDatabase(filter)
                itineraryViewModel.filterInsertionResult.observe(viewLifecycleOwner){ result ->
                    if (result){
                        // make Toast
                        Toast.makeText(context, "New filter added", Toast.LENGTH_LONG).show()
                        itineraryViewModel.setFilter(filter)
                        Log.d("add filter fragment", itineraryViewModel.filter.value.toString())
                        // go back to itinerary page
                        navController.popBackStack()
                    }
                }
//                // make Toast
//                Toast.makeText(context, "New filter added", Toast.LENGTH_LONG).show()
//                // go back to itinerary page
//
//                navController.popBackStack()
                addFilterToDatabase(Filter(name=filterName))
                // make Toast
                Toast.makeText(context, "New filter added", Toast.LENGTH_LONG).show()
                // go back to itinerary page
                navController.popBackStack()
            }
        }

        binding.addFilterCancelButton.setOnClickListener {
            // go back to itinerary page
            navController.popBackStack()
        }
    }

    private fun addFilterToDatabase(filter: Filter) {
        //add typed filter shown (saved in LiveData (and object?)) to the database
        itineraryViewModel.insertFilter(filter)
    }

    private fun checkFilterName(name: String) : Boolean {
        if (name.isEmpty()) {
            binding.addFilterFilterEditText.error = "Enter a filter name."
            return false
        }
        else if (name.length > 20) {
            binding.addFilterFilterEditText.error = "Filter name is too long."
            return false
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}