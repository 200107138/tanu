package com.example.tanu.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.R
import com.example.tanu.SessionManager
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
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

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment.newInstance(sharedViewModel))
                .commit()
        }
    }

    fun navigateToMainActivity() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finish AuthActivity to prevent going back
    }
    fun resetSuccessFlags() {
        sharedViewModel._loginSuccess.postValue(false)
        sharedViewModel._registerSuccess.postValue(false)
    }
    fun navigateToRegister() {
        resetSuccessFlags()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, RegisterFragment.newInstance(sharedViewModel))
            .addToBackStack(null)
            .commit()
    }
    fun navigateToLogin() {
        resetSuccessFlags()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, LoginFragment.newInstance(sharedViewModel))
            .addToBackStack(null)
            .commit()
    }
}
