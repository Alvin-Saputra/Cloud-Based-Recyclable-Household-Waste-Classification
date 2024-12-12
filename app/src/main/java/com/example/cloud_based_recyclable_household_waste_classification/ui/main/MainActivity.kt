package com.example.cloud_based_recyclable_household_waste_classification.ui.main

import android.content.SharedPreferences
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.cloud_based_recyclable_household_waste_classification.R
import com.example.cloud_based_recyclable_household_waste_classification.data.pref.UserViewModelFactory
import com.example.cloud_based_recyclable_household_waste_classification.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: SharedPreferences

    private val viewModel by viewModels<MainViewModel> {
        UserViewModelFactory.getInstance(this, this.application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.getSession().observe(this) { user ->
            if (!user.isLogin || isTokenExpired(user.exp)) {
                viewModel.DeleteAllFromDatabase()
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        navView.itemActiveIndicatorColor = ColorStateList.valueOf(
            ContextCompat.getColor(this, R.color.base_color_100)
        )


        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_saved, R.id.navigation_map, R.id.navigation_account
            )
        )


        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val fragmentToOpen = intent.getStringExtra("fragmentToOpen")
        if (fragmentToOpen == "SavedFragment") {
            navController.navigate(R.id.navigation_saved)
        }
        else if(fragmentToOpen == "HomeFragment"){
            navController.navigate(R.id.navigation_home)
        }

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Clear back stack before navigating to Home
                    navController.popBackStack(R.id.navigation_home, false)
                    navController.navigate(R.id.navigation_home)
                    true
                }
                R.id.navigation_saved -> {
                    navController.navigate(R.id.navigation_saved)
                    true
                }
                R.id.navigation_map -> {
                    navController.navigate(R.id.navigation_map)
                    true
                }
                R.id.navigation_account -> {
                    navController.navigate(R.id.navigation_account)
                    true
                }
                else -> false
            }
        }

    }

    fun isTokenExpired(expirationTime: Int): Boolean {
        val currentTime = System.currentTimeMillis() / 1000

        return currentTime > expirationTime
    }

}