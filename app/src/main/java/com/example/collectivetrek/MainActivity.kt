package com.example.collectivetrek

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.collectivetrek.databinding.ActivityMainBinding
import com.example.collectivetrek.fragments.GalleryFragment
import com.example.collectivetrek.fragments.ItineraryFragment
import com.example.collectivetrek.fragments.ProfileFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.nav_host_fragment)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.welcomeFragment -> hideBottomNav()
                R.id.loginFragment -> hideBottomNav()
                R.id.resetPasswordFragment -> hideBottomNav()
                R.id.group -> hideBottomNav()
                R.id.registerFragment -> hideBottomNav()
                R.id.createGroup -> hideBottomNav()
                else -> showBottomNav()
            }
        }
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile_item -> {
                    navController.navigate(R.id.profileFragment)
                }

                R.id.gallery_item -> {
                    navController.navigate(R.id.galleryFragment2)
                }

                R.id.itinerary_item -> {
                    navController.navigate(R.id.itineraryFragment)
                }

                else -> false
            }
            true
        }
    }

    private fun hideBottomNav() {
        binding.bottomNavigation.visibility = View.GONE
    }

    private fun showBottomNav() {
        binding.bottomNavigation.visibility = View.VISIBLE
    }
}
