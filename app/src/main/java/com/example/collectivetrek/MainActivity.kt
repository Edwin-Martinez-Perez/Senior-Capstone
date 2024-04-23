package com.example.collectivetrek

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        bottomNav = findViewById(R.id.bottom_navigation)

        // Set the initial selected item
        bottomNav.selectedItemId = R.id.welcomeFragment

        // Set up the item selected listener
        bottomNav.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.profileFragment -> {
                    navController.navigate(R.id.profileFragment)
                    true
                }

                R.id.galleryFragment -> {
                    navController.navigate(R.id.photoUploadFragment)
                    true
                }

                R.id.mapFragment -> {
                    navController.navigate(R.id.mapFragment)
                    true
                }

                else -> false
            }
        }
    }
}