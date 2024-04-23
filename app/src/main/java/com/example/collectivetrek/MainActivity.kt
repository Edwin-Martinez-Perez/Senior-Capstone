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

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
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


//package com.example.collectivetrek
//
//import android.os.Bundle
//import androidx.appcompat.app.AppCompatActivity
//import androidx.fragment.app.Fragment
//import com.example.collectivetrek.databinding.ActivityMainBinding
//import com.example.collectivetrek.fragments.ProfileFragment
//import com.example.collectivetrek.fragments.WelcomeFragment
//
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var binding : ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//        replaceFragment(WelcomeFragment())
//
//        binding.bottomNavigation.setOnItemSelectedListener {
//            when(it.itemId){
//                R.id.profile_item -> replaceFragment(ProfileFragment())
////                R.id.gallery_item -> replaceFragment(PhotoUploadFragment())
//                R.id.map_item -> replaceFragment(MapFragment())
////                R.id.itinerary_item -> replaceFragment(ItinteraryFragment())
//
//                else ->{
//
//                }
//            }
//            true
//        }
//    }
//
//    private fun replaceFragment(fragment : Fragment) {
//        val fragmentManager = supportFragmentManager
//        val fragmentTransaction = fragmentManager.beginTransaction()
//        fragmentTransaction.replace(R.id.frame_layout, fragment)
//        fragmentTransaction.commit()
//    }
//
//}
//import android.os.Bundle
//import android.view.View
//import androidx.appcompat.app.AppCompatActivity
//import androidx.navigation.NavController
//import androidx.navigation.fragment.NavHostFragment
//import androidx.navigation.ui.setupWithNavController
//import com.google.android.material.bottomnavigation.BottomNavigationView
//
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var navController: NavController
//    private lateinit var bottomNav: BottomNavigationView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val navHostFragment = supportFragmentManager
//            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        navController = navHostFragment.navController
//
//        bottomNav = findViewById(R.id.bottom_navigation)
//
//        // Setup bottom navigation with navController
//        bottomNav.setupWithNavController(navController)
//
//        // Set up a listener for when the destination changes
//        navController.addOnDestinationChangedListener { _, destination, _ ->
//            // Check if the destination is one where you want to hide the bottom navigation
//            val hideBottomNavDestinations = setOf(R.id.loginFragment, R.id.registerFragment,
//                R.id.resetPasswordFragment, R.id.welcomeFragment)
//
//            if (hideBottomNavDestinations.contains(destination.id)) {
//                bottomNav.visibility = View.GONE
//            } else {
//                bottomNav.visibility = View.VISIBLE
//            }
//        }
////         Set up the item selected listener
//        bottomNav.setOnItemSelectedListener { menuItem ->
//            when (menuItem.itemId) {
//                R.id.itinerary_item -> {
//                    navController.navigate(R.id.itineraryFragment)
//                    true
//                }
//                R.id.profile_item -> {
//                    navController.navigate(R.id.profileFragment)
//                    true
//                }
//                R.id.gallery_item -> {
//                    navController.navigate(R.id.photoUploadFragment)
//                    true
//                }
//                R.id.map_item -> {
//                    navController.navigate(R.id.mapFragment)
//                    true
//                }
//                else -> false
//            }
//        }
//    }
//}
