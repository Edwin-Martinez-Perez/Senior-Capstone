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
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.welcomeFragment -> {
                    navController.navigate(R.id.welcomeFragment)
                    true
                }
                R.id.resetPasswordFragment -> {
                    navController.navigate(R.id.resetPasswordFragment)
                    true
                }
                R.id.registerFragment -> {
                    navController.navigate(R.id.registerFragment)
                    true
                }
                else -> false
            }
        }

        // Navigate to the start destination when the app starts
        navController.navigate(R.id.welcomeFragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}