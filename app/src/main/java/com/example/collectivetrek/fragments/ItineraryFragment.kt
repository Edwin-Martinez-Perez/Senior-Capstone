package com.example.collectivetrek.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}