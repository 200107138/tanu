package com.example.tanu.ui.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.R
import com.example.tanu.SessionManager
import com.example.tanu.data.Repository.MainRepository
import com.example.tanu.data.Retrofit.ApiClient
import com.example.tanu.databinding.ActivityAuthBinding
import com.example.tanu.ui.main.MainActivity

// AuthActivity.kt
class AuthActivity : AppCompatActivity() {

    private lateinit var sharedViewModel: AuthViewModel
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auth)

        val apiService = ApiClient().getApiService(this)
        val sessionManager = SessionManager(this)
        val repository = MainRepository(apiService, sessionManager)
        val sharedFactory = AuthViewModelFactory(repository)
        sharedViewModel = ViewModelProvider(this, sharedFactory).get(AuthViewModel::class.java)

        sharedViewModel.loginStatus.observe(this, Observer { loginStatus ->
            if (loginStatus) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            } else {
                // Handle login failure if needed
            }
        })

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment.newInstance(sharedViewModel))
                .commit()
        }
    }

    fun navigateToRegister() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, RegisterFragment.newInstance(sharedViewModel))
            .addToBackStack(null)
            .commit()
    }
}
