package com.example.collectivetrek.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.collectivetrek.R
import com.example.collectivetrek.database.User
import com.example.collectivetrek.databinding.RegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class RegisterFragment : Fragment() {

    private lateinit var binding: RegisterBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = RegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get instance from Firebase
        auth = FirebaseAuth.getInstance()

        // Binding for register button
        binding.registerButton.setOnClickListener {
            signUp()
        }

        // Binding for the back button
        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_welcomeFragment)
        }
    }

    private fun signUp() {
        // Create variables
        val firstNameTextInput = binding.addFirstNameEditText
        val lastNameTextInput = binding.addLastNameEditText
        val emailTextInput = binding.addEmailEditText
        val passwordTextInput = binding.addPasswordEditText

        val firstNameEditText = firstNameTextInput.editText
        val lastNameEditText = lastNameTextInput.editText
        val emailEditText = emailTextInput.editText
        val passwordEditText = passwordTextInput.editText

        val firstName = firstNameEditText?.text.toString()
        val lastName = lastNameEditText?.text.toString()
        val email = emailEditText?.text.toString()
        val password = passwordEditText?.text.toString()

        // Make sure all fields are full
        if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Authentication for creating user with email and password
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Create variables and save information to user
                    val user = auth.currentUser
                    val userId = user?.uid
                    val newUser = User(firstName, lastName, email)
                    // Validate user ID
                    if (userId != null) {
                        // Get database reference
                        val databaseReference = FirebaseDatabase.getInstance().getReference("Users")
                        databaseReference.child(userId).setValue(newUser)
                            .addOnSuccessListener {
                                // Toast for when user registers
                                Toast.makeText(context, "User registered!", Toast.LENGTH_SHORT)
                                    .show()
                                findNavController().navigate(R.id.action_registerFragment_to_welcomeFragment)
                            }
                            .addOnFailureListener { exception ->
                                // Toast if user cannot register
                                Log.e(TAG, "Failed to add user data to database", exception)
                                Toast.makeText(
                                    context,
                                    "Failed to register user",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                    // If user is null
                    } else {
                        Log.e(TAG, "User UID is null")
                        Toast.makeText(context, "Failed to register user", Toast.LENGTH_SHORT)
                            .show()
                    }
                } else {
                    // User creation failed
                    Log.e(TAG, "createUserWithEmailAndPassword:failure", task.exception)
                    Toast.makeText(
                        context,
                        "Authentication failed: ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    companion object {
        private const val TAG = "RegisterFragment"
    }
}