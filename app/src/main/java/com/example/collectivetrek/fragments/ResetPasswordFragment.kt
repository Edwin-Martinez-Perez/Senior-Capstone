package com.example.collectivetrek.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.navigation.fragment.findNavController
import com.example.collectivetrek.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ResetPasswordFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    // Create variables
    private var email: String? = null
    private var username: String? = null

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
        val view = inflater.inflate(R.layout.reset_password, container, false)

        // When focus is changed on email edit text
        view.findViewById<EditText>(R.id.add_email_edit_text)?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Save what the user wrote as email
                email = view.findViewById<EditText>(R.id.add_email_edit_text)?.text.toString()
            }
        }

        // When focus is changed on username edit text
        view.findViewById<EditText>(R.id.add_username_edit_text)?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Save what the user wrote as username
                username = view.findViewById<EditText>(R.id.add_email_edit_text)?.text.toString()
            }
        }

        // Set onClickListener for the next button to lead to the reset password with link fragment
        view.findViewById<View>(R.id.next_button)?.setOnClickListener{
            findNavController().navigate(R.id.action_resetPasswordFragment_to_resetPasswordWithLinkFragment)
        }

        // Set onClickListener for the back button to lead to the welcome fragment
        view.findViewById<View>(R.id.back_button)?.setOnClickListener{
            findNavController().navigate(R.id.action_resetPasswordFragment_to_welcomeFragment)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResetPasswordFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}