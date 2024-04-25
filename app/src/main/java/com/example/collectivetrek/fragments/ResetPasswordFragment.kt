package com.example.collectivetrek.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.os.CountDownTimer
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
    private var isButtonClickable = true
    private var countdownTimer: CountDownTimer? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ResetPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get Firebase instance
        auth = FirebaseAuth.getInstance()

        binding.countdownTextView.visibility = View.GONE

        // When login button is pushed, go to login function
        binding.sendButton.setOnClickListener {
            if (isButtonClickable) {
                sendEmail()
            }
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

        if (email.isBlank()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Authentication for sending an email
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                    Toast.makeText(context, "Email sent!", Toast.LENGTH_SHORT).show()
                    disableButtonWithCountdown(10)
                } else {
                    Toast.makeText(context, "Try another email or create account.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun disableButtonWithCountdown(seconds: Long) {
        isButtonClickable = false
        val initialButtonText = binding.sendButton.text
        binding.sendButton.isEnabled = false
        binding.sendButton.text = initialButtonText
        binding.countdownTextView.visibility = View.VISIBLE

        countdownTimer = object : CountDownTimer(seconds * 1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsRemaining = millisUntilFinished / 1000
                binding.countdownTextView.text = "Try again in $secondsRemaining seconds"
            }

            override fun onFinish() {
                isButtonClickable = true
                binding.sendButton.isEnabled = true
                binding.sendButton.text = initialButtonText
                binding.countdownTextView.visibility = View.GONE
            }
        }

        countdownTimer?.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
    }
}
