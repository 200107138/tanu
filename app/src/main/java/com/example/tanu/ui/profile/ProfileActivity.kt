package com.example.tanu.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.tanu.SessionManager
import com.example.tanu.data.adapters.ProfilePostAdapter
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var profilePostAdapter: ProfilePostAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get userId from arguments
        val userId = intent.getStringExtra("userId") ?: ""

        // Initialize ViewModel
        val apiClient = ApiClient()
        val sessionManager = SessionManager(this)
        val repository = MainRepository(apiClient.getApiService(this), sessionManager)
        viewModel = ViewModelProvider(this, ProfileViewModelFactory(repository)).get(
            ProfileViewModel::class.java)

        // Initialize RecyclerView adapter
        profilePostAdapter = ProfilePostAdapter(this)

        // Set RecyclerView adapter
        binding.recyclerView.adapter = profilePostAdapter

        // Set GridLayoutManager with spanCount 3
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)

        // Observe postsLiveData
        viewModel.postsLiveData.observe(this, Observer { posts ->
            posts?.let {
                // Update RecyclerView with the list of posts
                profilePostAdapter.submitList(posts)
            }
        })

        // Call getPostsByUserId API with the user ID
        viewModel.getPostsByUserId(userId)
    }
}
