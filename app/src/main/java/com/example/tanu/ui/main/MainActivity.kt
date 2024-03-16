package com.example.tanu.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.R
import com.example.tanu.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        Log.d("Sample", "Loading Auth Token")
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    viewModel.changeFragment(HomeFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_map -> {
                    viewModel.changeFragment(MapFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_conversations -> {
                    viewModel.changeFragment(ChatRoomsFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_profile -> {
                    viewModel.changeFragment(ProfileFragment())
                    return@setOnItemSelectedListener true
                }
                else -> false
            }
        }


        if (savedInstanceState == null) {
            // Set the initial fragment
            viewModel.changeFragment(HomeFragment())
        }

        viewModel.currentFragment.observe(this) { fragment ->
            replaceFragment(fragment)
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}