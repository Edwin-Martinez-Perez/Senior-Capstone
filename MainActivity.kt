package com.example.collectivetrek

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.collectivetrek.databinding.ActivityMainBinding
import com.example.collectivetrek.fragments.GalleryFragment
import com.example.collectivetrek.fragments.ItineraryFragment
import com.example.collectivetrek.fragments.LoginFragment
import com.example.collectivetrek.fragments.ProfileFragment
import com.example.collectivetrek.fragments.RegisterFragment
import com.example.collectivetrek.fragments.ResetPasswordFragment
import com.example.collectivetrek.fragments.WelcomeFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav?.visibility = View.GONE
//        replaceFragment(WelcomeFragment())
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.profile_item -> {
                    replaceFragment(ProfileFragment())
                    updateBottomNavigationVisibility()
                }
                R.id.gallery_item -> {
                    replaceFragment(GalleryFragment())
                    updateBottomNavigationVisibility()
                }
                R.id.map_item -> {
                    replaceFragment(GalleryFragment())
                    updateBottomNavigationVisibility()
                }
                R.id.itinerary_item -> {
                    replaceFragment(ItineraryFragment())
                    updateBottomNavigationVisibility()
                }

                else -> {

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager //creates fragment manager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment, fragment)
        transaction.commit()
    }

    private fun updateBottomNavigationVisibility() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)

        if (currentFragment is WelcomeFragment || currentFragment is LoginFragment || currentFragment is RegisterFragment ||
            currentFragment is ResetPasswordFragment) {
            binding.bottomNavigation.visibility = View.GONE
        } else {
            binding.bottomNavigation.visibility = View.VISIBLE
        }
    }
}
