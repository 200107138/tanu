package com.example.tanu.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.tanu.SessionManager
import com.example.tanu.data.UserHolder
import com.example.tanu.data.adapters.ProfilePostAdapter
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.FragmentProfileBinding
import com.example.tanu.ui.main.LeaderboardActivity


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var profilePostAdapter: ProfilePostAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        val apiClient = ApiClient()
        val sessionManager = SessionManager(requireContext())
        val repository = MainRepository(apiClient.getApiService(requireContext()), sessionManager)
        viewModel = ViewModelProvider(this, ProfileViewModelFactory(repository)).get(
            ProfileViewModel::class.java)

        // Initialize RecyclerView adapter
        profilePostAdapter = ProfilePostAdapter(requireContext())

        // Set RecyclerView adapter
        binding.recyclerView.adapter = profilePostAdapter

        // Set GridLayoutManager with spanCount 3
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.leaderboard.setOnClickListener {
            // Open LeaderboardActivity
            startActivity(Intent(requireContext(), LeaderboardActivity::class.java))
        }
        // Observe postsLiveData
        viewModel.postsLiveData.observe(viewLifecycleOwner, Observer { posts ->
            posts?.let {
                // Update RecyclerView with the list of posts
                profilePostAdapter.submitList(posts)
            }
        })
        viewModel.userInfoLiveData.observe(viewLifecycleOwner, Observer { userInfo ->
            userInfo?.let {
                // Update UI with user information
                Glide.with(requireContext())
                    .load(userInfo.user.avatarUrl)
                    .into(binding.avatar)
                binding.email.text = userInfo.user.email
            }
        })
        // Get user ID from UserHolder
        val userId = UserHolder.userId ?: ""

        // Call getPostsByUserId API with the user ID
        viewModel.getPostsByUserId(userId)
        // Call getUserInfo API with the user ID
        viewModel.getUserInfo(userId)
    }
}
