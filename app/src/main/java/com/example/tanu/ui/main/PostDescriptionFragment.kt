package com.example.tanu.ui.main

// PostDescriptionFragment.kt
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.tanu.SessionManager
import com.example.tanu.data.adapters.PostMediaAdapter
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.FragmentPostDescriptionBinding
import com.example.tanu.ui.messages.MessageActivity
import com.example.tanu.ui.profile.ProfileActivity

class PostDescriptionFragment : Fragment() {

    private lateinit var binding: FragmentPostDescriptionBinding
    private lateinit var viewModel: PostDescriptionViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDescriptionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val apiClient = ApiClient()
        val sessionManager = SessionManager(requireContext())
        val repository = MainRepository(apiClient.getApiService(requireContext()), sessionManager)
        viewModel = ViewModelProvider(this, PostDescriptionViewModelFactory(repository)).get(
            PostDescriptionViewModel::class.java)

        val postId = arguments?.getString("postId") ?: ""

        // Call the getPostInfo API
        viewModel.getPostInfo(postId)

        // Observe the getPostInfo LiveData
        viewModel.postInfoLiveData.observe(viewLifecycleOwner) { post ->
            // Update the UI with post info
            binding.description.text = post.description
            binding.email.text = post.user.email
            // Set click listener for message button
            binding.messageButton.setOnClickListener {
                // Create an Intent to start the MessageActivity
                val intent = Intent(requireContext(), MessageActivity::class.java)
                intent.putExtra("postId", post.id)
                intent.putExtra("receiverId", post.user.id)
                // Start the MessageActivity
                startActivity(intent)
            }
            binding.avatar.setOnClickListener {
                val profileIntent = Intent(requireContext(), ProfileActivity::class.java)
                profileIntent.putExtra("userId", post.user.id)
                startActivity(profileIntent)
            }
        }
    }

    companion object {
        private const val POST_ID_KEY = "postId"

        fun newInstance(postId: String): PostDescriptionFragment {
            val fragment = PostDescriptionFragment()
            val bundle = Bundle().apply {
                putString(POST_ID_KEY, postId)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}