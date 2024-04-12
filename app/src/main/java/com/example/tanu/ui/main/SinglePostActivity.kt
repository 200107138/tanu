package com.example.tanu.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.tanu.R
import com.example.tanu.SessionManager
import com.example.tanu.data.adapters.PostAdapter
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.ActivityMediaUploadBinding
import com.example.tanu.databinding.ActivitySinglePostBinding
import com.example.tanu.databinding.FragmentHomeBinding

class SinglePostActivity : AppCompatActivity() {

    private lateinit var viewModel: SinglePostViewModel
    private lateinit var binding: ActivitySinglePostBinding
    private lateinit var postAdapter: PostAdapter
    private var position: Int = 0
    private lateinit var posts: List<Post>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySinglePostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiClient = ApiClient()
        val sessionManager = SessionManager(this)
        val repository = MainRepository(apiClient.getApiService(this), sessionManager)
        viewModel = ViewModelProvider(this, SinglePostViewModelFactory(repository)).get(SinglePostViewModel::class.java)


        position = intent.getIntExtra("position", 0)
        posts = intent.getSerializableExtra("posts") as List<Post>

        // Initialize ViewPager2 adapter
        postAdapter = PostAdapter(this).apply {
            setPosts(posts)
        }

        // Set ViewPager2 adapter
        binding.posts.adapter = postAdapter

        // Scroll to the selected position
        binding.posts.post {
            binding.posts.currentItem = position
        }
    }

}
