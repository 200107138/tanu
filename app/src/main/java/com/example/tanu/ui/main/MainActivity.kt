package com.example.tanu.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.R
import com.example.tanu.databinding.ActivityMainBinding
import com.example.tanu.ui.forum.ForumFragment
import com.example.tanu.ui.messages.ChatRoomsFragment
import com.example.tanu.ui.profile.ProfileFragment

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    viewModel.changeFragment(HomeFragment())
                    return@setOnItemSelectedListener true
                }
                R.id.navigation_forum -> {
                    viewModel.changeFragment(ForumFragment())
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
                R.id.navigation_upload -> {
                    startActivity(Intent(this, PostPostActivity::class.java))
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