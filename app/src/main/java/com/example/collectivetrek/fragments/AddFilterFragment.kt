package com.example.collectivetrek.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import com.example.collectivetrek.ItineraryRepository
import com.example.collectivetrek.ItineraryViewModel
import com.example.collectivetrek.ItineraryViewModelFactory
import com.example.collectivetrek.SharedViewModel
import com.example.collectivetrek.database.Filter
import com.example.collectivetrek.databinding.FragmentAddFilterBinding


class AddFilterFragment : Fragment() {
    private var _binding: FragmentAddFilterBinding? = null
    private val binding get() = _binding!!

    private val itineraryViewModel: ItineraryViewModel by activityViewModels() {
        ItineraryViewModelFactory(repository = ItineraryRepository())
    }

    private val groupIdViewModel: SharedViewModel by viewModels({requireActivity()})
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
        itineraryViewModel.setGroupId(groupIdViewModel.sharedGroupId.value.toString())


        binding.addFilterAddButton.setOnClickListener {
            // retrieve filter from edit text and validate
            var filterName = binding.addFilterFilterEditText.editText?.text.toString()
            Log.d("add filter fragment",filterName)
            if (checkFilterName(filterName)) {
                // store in database
                filterName = filterName.replaceFirstChar { c->
                    c.titlecase()
                }
                val filter = Filter(name=filterName)
                addFilterToDatabase(filter)
                itineraryViewModel.filterInsertionResult.observe(viewLifecycleOwner){ result ->
                    if (result){
                        // make Toast
                        Toast.makeText(context, "New filter added", Toast.LENGTH_LONG).show()
                        Log.d("add filter fragment", filter.id.toString())
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