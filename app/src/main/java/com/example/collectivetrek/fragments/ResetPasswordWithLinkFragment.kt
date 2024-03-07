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

class ResetPasswordWithLinkFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    // Create a variable for the new password
    private var newPassword: String? = null

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
        val view = inflater.inflate(R.layout.reset_password_with_link, container, false)

        // ONCLICKLISTENER FOR RESET IN DATABASE, change database to have updated information

        // Set onClickListener for the reset button to reset the password in the
        // database and return to the welcome fragment
        view.findViewById<View>(R.id.reset_button)?.setOnClickListener{
            findNavController().navigate(R.id.action_resetPasswordWithLinkFragment_to_welcomeFragment)
        }

        // Set onClickListener for the home button to lead to the welcome fragment
        view.findViewById<View>(R.id.home_button)?.setOnClickListener{
            findNavController().navigate(R.id.action_resetPasswordWithLinkFragment_to_welcomeFragment)
        }

        // When focus is changed on password edit text
        view.findViewById<EditText>(R.id.password_edit_text)?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Save what the user wrote as newPassword
                newPassword = view.findViewById<EditText>(R.id.password_edit_text)?.text.toString()
            }
        }

        // When focus is changed on new password edit text
        view.findViewById<EditText>(R.id.new_password_edit_text)?.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) {
                // Save the re-entered password as a variable
                val reenterPassword =
                    view.findViewById<EditText>(R.id.new_password_edit_text)?.text.toString()
                // Check to see if the passwords do not match
                if (newPassword != reenterPassword) {
                    // Make a toast message to show that the passwords do not match
                    Toast.makeText(
                        context,
                        "Passwords do not match, please try again.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        // delete original password from database and add password

        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResetPasswordWithLinkFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}