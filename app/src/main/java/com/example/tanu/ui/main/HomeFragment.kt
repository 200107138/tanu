package com.example.tanu.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager

import com.example.tanu.R
import com.example.tanu.SessionManager
import com.example.tanu.data.adapters.PostListAdapter
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var postAdapter: PostListAdapter
    private var originalPosts: List<Post> = emptyList() // Store the original list of posts

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel, Repository, and API client
        val apiClient = ApiClient()
        val sessionManager = SessionManager(requireContext())
        val repository = MainRepository(apiClient.getApiService(requireContext()), sessionManager)
        viewModel = ViewModelProvider(this, HomeViewModelFactory(repository)).get(HomeViewModel::class.java)

        // Initialize RecyclerView adapter
        postAdapter = PostListAdapter(requireContext())
        binding.posts.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.posts.adapter = postAdapter

        // Observe postsLiveData
        viewModel.postsLiveData.observe(viewLifecycleOwner, Observer { posts ->
            posts?.let {
                originalPosts = posts // Save the original list of posts
                postAdapter.setPosts(posts)
            }
        })

        // Call getPosts API
        viewModel.getPosts()

        // Set up SearchView listener
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterPosts(it) // Filter posts when the text changes
                }
                return false
            }
        })
    }

    private fun filterPosts(query: String) {
        val filteredPosts = originalPosts.filter { post ->
            post.title?.contains(query, ignoreCase = true) ?: false ||
                    post.description?.contains(query, ignoreCase = true) ?: false
        }
        postAdapter.submitList(filteredPosts)
    }
}
