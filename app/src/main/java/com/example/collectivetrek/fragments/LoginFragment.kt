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

class LoginFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    // Create a variable for the username and password
    private var username: String? = null
    private var password: String? = null




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
        val view = inflater.inflate(R.layout.login, container, false)

        // When focus is changed on username edit text
        view.findViewById<EditText>(R.id.username_edit_text)?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Save what the user wrote as username
                username = view.findViewById<EditText>(R.id.username_edit_text)?.text.toString()
            }
        }

        // When focus is changed on password edit text
        view.findViewById<EditText>(R.id.password_edit_text)?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Save what the user wrote as password
                password = view.findViewById<EditText>(R.id.password_edit_text)?.text.toString()
            }
        }

        // Set onClickListener for the login button to lead to the group fragment
        view.findViewById<View>(R.id.login_button)?.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_group)

            // CHECK DATABASE TO SEE IF USERNAME AND PASSWORD MATCH

        }

        // Set onClickListener for the back button to lead to the welcome fragment
        view.findViewById<View>(R.id.back_button)?.setOnClickListener{
            findNavController().navigate(R.id.action_loginFragment_to_welcomeFragment)
        }

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LoginFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}