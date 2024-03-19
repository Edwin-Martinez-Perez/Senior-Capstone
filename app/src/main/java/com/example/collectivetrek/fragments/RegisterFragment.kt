package com.example.collectivetrek.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.collectivetrek.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class RegisterFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    // Create variables
    private var first_name: String? = null
    private var last_name: String? = null
    private var username: String? = null
    private var email: String? = null
    private var password: String? = null
    private var reenter_password: String? = null

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
        val view = inflater.inflate(R.layout.register, container, false)

        // When focus is changed on first name edit text
        view.findViewById<EditText>(R.id.add_first_name_edit_text)?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Save what the user wrote as first_name
                first_name = view.findViewById<EditText>(R.id.add_first_name_edit_text)?.text.toString()
            }
        }

        // When focus is changed on last name edit text
        view.findViewById<EditText>(R.id.add_last_name_edit_text)?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Save what the user wrote as last_name
                last_name = view.findViewById<EditText>(R.id.add_last_name_edit_text)?.text.toString()
            }
        }

        // When focus is changed on username edit text
        view.findViewById<EditText>(R.id.add_username_edit_text)?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Save what the user wrote as username
                username = view.findViewById<EditText>(R.id.add_username_edit_text)?.text.toString()
            }
        }

        // When focus is changed on email edit text
        view.findViewById<EditText>(R.id.add_email_edit_text)?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Save what the user wrote as email
                email = view.findViewById<EditText>(R.id.add_email_edit_text)?.text.toString()
            }
        }

        // When focus is changed on password edit text
        view.findViewById<EditText>(R.id.add_password_edit_text)?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Save what the user wrote as password
                password = view.findViewById<EditText>(R.id.add_password_edit_text)?.text.toString()
            }
        }

        // When focus is changed on re-enter password edit text
        view.findViewById<EditText>(R.id.add_reenter_password_edit_text)?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Save the re-entered password as a variable
                val reenter_password =
                    view.findViewById<EditText>(R.id.add_reenter_password_edit_text)?.text.toString()
                // Check to see if the passwords do not match
                if (password != reenter_password) {
                    // Make a toast message to show that the passwords do not match
                    Toast.makeText(
                        context,
                        "Passwords do not match, please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // Set onClickListener for the register button to lead to the welcome fragment
        view.findViewById<View>(R.id.register_button)?.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_welcomeFragment)
        }

        // Set onClickListener for the back button to lead to the welcome fragment
        view.findViewById<View>(R.id.back_button)?.setOnClickListener{
            findNavController().navigate(R.id.action_registerFragment_to_welcomeFragment)
        }

        // email is the unique id, username does not need to be unique

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}