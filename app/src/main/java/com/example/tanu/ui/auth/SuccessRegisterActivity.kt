package com.example.tanu.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tanu.R
import com.example.tanu.databinding.ActivitySuccessPostPostBinding
import com.example.tanu.databinding.ActivitySuccessRegisterBinding
import com.example.tanu.ui.main.MainActivity


class SuccessRegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySuccessRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.loginPageButton.setOnClickListener {
            val intent = Intent(this, AuthActivity::class.java)
            // Add flags to clear previous activities and start a new task
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            finish()
        }
    }
}
