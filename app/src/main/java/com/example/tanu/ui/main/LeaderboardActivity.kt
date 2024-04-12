package com.example.tanu.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tanu.R
import com.example.tanu.SessionManager
import com.example.tanu.data.adapters.PostListAdapter
import com.example.tanu.data.adapters.ProfilePostAdapter
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.ActivityLeaderboardBinding

class LeaderboardActivity : AppCompatActivity() {

    private lateinit var viewModel: LeaderboardViewModel
    private lateinit var binding: ActivityLeaderboardBinding
    private lateinit var postListAdapter: PostListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiClient = ApiClient()
        val sessionManager = SessionManager(this)
        val repository = MainRepository(apiClient.getApiService(this), sessionManager)
        viewModel = ViewModelProvider(this, LeaderboardViewModelFactory(repository)).get(LeaderboardViewModel::class.java)

        setupRecyclerView()

        viewModel.leaderboardPosts.observe(this, Observer { posts ->
            posts?.let {
                postListAdapter.submitList(it)
            }
        })

        viewModel.getLeaderboardPosts()
    }

    private fun setupRecyclerView() {
        postListAdapter = PostListAdapter(this)

        // Set RecyclerView adapter
        binding.recyclerView.adapter = postListAdapter

        // Set GridLayoutManager with spanCount 3
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)
    }
}
