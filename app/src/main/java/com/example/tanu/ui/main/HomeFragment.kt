package com.example.tanu.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.data.adapters.PostsAdapter
import com.example.tanu.data.models.Post
import com.example.tanu.R
import com.example.tanu.SessionManager
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
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


    }



}
