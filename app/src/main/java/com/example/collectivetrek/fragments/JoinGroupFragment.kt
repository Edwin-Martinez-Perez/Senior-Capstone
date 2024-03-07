package com.example.collectivetrek.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.collectivetrek.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class JoinGroupFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout
        val view = inflater.inflate(R.layout.join_group, container, false)

        // Set onClickListener for the join button to lead to the group fragment
        view.findViewById<View>(R.id.join_button)?.setOnClickListener{
            findNavController().navigate(R.id.action_joinGroupFragment_to_group)
        }

        // Set onClickListener for the back button to lead to the group fragment
        view.findViewById<View>(R.id.back_button)?.setOnClickListener{
            findNavController().navigate(R.id.action_joinGroupFragment_to_group)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            JoinGroupFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}