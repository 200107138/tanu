package com.example.tanu.ui.profile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tanu.SessionManager
import com.example.tanu.data.UserHolder
import com.example.tanu.data.adapters.ProfilePostListAdapter
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient

import com.example.tanu.databinding.ActivityProfileBinding
import com.example.tanu.databinding.ActivityProfilePostListBinding

class ProfilePostListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfilePostListBinding
    private lateinit var postListAdapter: ProfilePostListAdapter
    private lateinit var viewModel: ProfilePostListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfilePostListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiClient = ApiClient()
        val sessionManager = SessionManager(this)
        val repository = MainRepository(apiClient.getApiService(this), sessionManager)
        viewModel = ViewModelProvider(this, ProfilePostListViewModelFactory(repository)).get(
            ProfilePostListViewModel::class.java)

        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        setupRecyclerView()

        val postType = intent.getStringExtra("postType") ?: ""
        when (postType) {
            "active" -> binding.status.text = "белсенді"
            "inactive" -> binding.status.text = "белсенді емес"
            else -> binding.status.text = "" // You can set a default value if needed
        }
        // Observe active posts LiveData
        viewModel.postsLiveData.observe(this, Observer { posts ->
            val filteredPosts = when (postType) {
                "active" -> posts.filter { it.status == "active" }
                "inactive" -> posts.filter { it.status == "inactive" }
                else -> posts // If postType is neither "active" nor "inactive", submit all posts
            }
            postListAdapter.submitList(filteredPosts)
        })


        viewModel.getUserPosts()
    }

    private fun setupRecyclerView() {
        postListAdapter = ProfilePostListAdapter(
            onActivateClickListener = { postId -> viewModel.putPostStatus(postId, "active") },
            onDeactivateClickListener = { postId -> viewModel.putPostStatus(postId, "inactive") }
        )
        binding.profilePostsListRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ProfilePostListActivity)
            adapter = postListAdapter
        }
    }
}