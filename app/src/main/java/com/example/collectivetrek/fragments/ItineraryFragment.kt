package com.example.collectivetrek.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.collectivetrek.R
import com.example.collectivetrek.databinding.FragmentItineraryBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ItineraryFragment : Fragment() {

    private var _binding: FragmentItineraryBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            //anything not related to view hierarchy
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentItineraryBinding.inflate(inflater, container) //? attachToRoot false

        binding?.apply {
            // viewmodel, lifecycle owner, recyclerview
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //show list of events

        //show list of filters

        //buttons
        binding.addEventButton.setOnClickListener {
            // go to add event fragment
            // findNavController().navigate()
        }

        binding.addFilterButton.setOnClickListener {
            // go to add filter fragment
            // findNavController().navigate()
        }


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}