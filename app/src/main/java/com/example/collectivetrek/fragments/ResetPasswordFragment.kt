package com.example.collectivetrek.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.collectivetrek.R
import com.example.collectivetrek.databinding.ResetPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordFragment : Fragment() {

    private lateinit var binding: ResetPasswordBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = ResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get Firebase instance
        auth = FirebaseAuth.getInstance()

        // When login button is pushed, go to login function
        binding.sendButton.setOnClickListener {
            sendEmail()
        }

        // When back button is pushed, go to welcome fragment
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_resetPasswordFragment_to_welcomeFragment)
        }
    }

    private fun sendEmail() {
        // Create variables
        val emailTextInput = binding.addEmailEditText

        val emailEditText = emailTextInput.editText

        val email = emailEditText?.text.toString()

        // Authentication for sending an email
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                    Toast.makeText(context, "Email sent!", Toast.LENGTH_SHORT)
                        .show()
                }
                else{
                    Toast.makeText(context, "Try another email or create account.", Toast.LENGTH_SHORT)
                        .show()
                }
            }
    }
}