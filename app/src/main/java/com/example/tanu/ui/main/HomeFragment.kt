package com.example.tanu.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.data.Adapters.PostsAdapter
import com.example.tanu.data.Models.Post
import com.example.tanu.R
import com.example.tanu.SessionManager
import com.example.tanu.data.Repository.MainRepository
import com.example.tanu.data.Retrofit.ApiClient
import com.example.tanu.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var postsAdapter: PostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiClient = ApiClient()
        val sessionManager = SessionManager(requireContext()) // Create SessionManager instance
        val repository = MainRepository(apiClient.getApiService(requireContext()), sessionManager)
        viewModel = ViewModelProvider(this, HomeViewModelFactory(repository)).get(HomeViewModel::class.java)
        observeViewModel()
        viewModel.fetchData()
    }

    private fun observeViewModel() {
        viewModel.postsLiveData.observe(viewLifecycleOwner) { posts ->
            posts?.let {
                initCardStackView(it)
            }
        }

        viewModel.errorLiveData.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.commentStatusLiveData.observe(viewLifecycleOwner) { commentPosted ->
            if (commentPosted) {
                Toast.makeText(requireContext(), "Comment posted successfully", Toast.LENGTH_SHORT).show()
                // Optionally, update UI or take any other action
            } else {
                // Handle if comment posting failed
                Toast.makeText(requireContext(), "Failed to post comment", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initCardStackView(posts: ArrayList<Post>) {
        postsAdapter = PostsAdapter(requireContext(), posts) { postId, commentText ->
            viewModel.postComment(postId, commentText)
        }
        val viewPager2 = binding.viewPager2
        viewPager2.adapter = postsAdapter
    }
}
