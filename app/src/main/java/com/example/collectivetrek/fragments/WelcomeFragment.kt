package com.example.collectivetrek.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.collectivetrek.R
import com.example.collectivetrek.databinding.WelcomePageBinding
import com.example.collectivetrek.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class WelcomeFragment : Fragment() {

    // Set variables
    private var _binding: WelcomePageBinding? = null
    private val binding get() = _binding!!
    private var bottomNavigationView: BottomNavigationView? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WelcomePageBinding.inflate(inflater, container, false)
        val view = binding.root
        // Set onClickListener for the login button to lead to the login fragment
        binding.loginButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
        }
        // Set onClickListener for the register button to lead to the register fragment
        binding.registerButton.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeFragment_to_registerFragment)
        }
        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
    }
}