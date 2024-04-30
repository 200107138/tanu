package com.example.tanu.ui.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.R
import com.example.tanu.SessionManager
import com.example.tanu.data.UserHolder
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.ActivityProfileEditUserNameBinding
import com.google.android.material.snackbar.Snackbar

class ProfileEditUserNameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileEditUserNameBinding
    private lateinit var viewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileEditUserNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        val apiClient = ApiClient()
        val sessionManager = SessionManager(this)
        val repository = MainRepository(apiClient.getApiService(this), sessionManager)
        viewModel = ViewModelProvider(this, ProfileViewModelFactory(repository)).get(
            ProfileViewModel::class.java)

        // Set click listener for submit button
        binding.submitButton.setOnClickListener {
            val newName = binding.nameEditText.text.toString().trim()
            if (newName.isNotEmpty()) {
                viewModel.putUserName(newName)
            } else {
                Snackbar.make(binding.root, "Name cannot be empty", Snackbar.LENGTH_SHORT).show()
            }
        }

        // Observe putUserNameResultLiveData
        viewModel.putUserNameResultLiveData.observe(this) { result ->
            if (result == "success") {
                // Set result as OK
                setResult(AppCompatActivity.RESULT_OK)
                // Close the activity
                finish()
            } else {
                Snackbar.make(binding.root, "Failed to update name", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}