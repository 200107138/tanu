package com.example.tanu.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tanu.R
import com.example.tanu.databinding.ActivityProfileEditUserNameBinding
import com.example.tanu.databinding.ActivitySuccessPostPostBinding
import com.example.tanu.ui.profile.ProfileViewModel


class SuccessPostPostActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuccessPostPostBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessPostPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mainPageButton.setOnClickListener {
            // Open MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            // Finish the current activity
            finish()
        }
    }
}
