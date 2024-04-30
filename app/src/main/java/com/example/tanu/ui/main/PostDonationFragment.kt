// PostDiscussionFragment.kt
package com.example.tanu.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tanu.SessionManager
import com.example.tanu.data.adapters.DiscussionListAdapter
import com.example.tanu.data.adapters.PostMediaAdapter
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.FragmentPostDiscussionBinding
import com.example.tanu.databinding.FragmentPostDonationBinding
import com.example.tanu.ui.forum.DiscussionActivity
import com.example.tanu.ui.forum.NewDiscussionActivity
import com.example.tanu.ui.messages.MessageActivity
import com.example.tanu.ui.profile.ProfileActivity

class PostDonationFragment : Fragment() {

    private lateinit var viewModel: PostDonationViewModel
    private lateinit var binding: FragmentPostDonationBinding

    companion object {
        private const val POST_ID_KEY = "postId"

        fun newInstance(postId: String): PostDonationFragment {
            val fragment = PostDonationFragment()
            val bundle = Bundle().apply {
                putString(POST_ID_KEY, postId)
            }
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPostDonationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postId = arguments?.getString("postId") ?: ""

        val apiClient = ApiClient()
        val sessionManager = SessionManager(requireContext())
        val repository = MainRepository(apiClient.getApiService(requireContext()), sessionManager)
        viewModel = ViewModelProvider(this, PostDonationViewModelFactory(repository)).get(
            PostDonationViewModel::class.java)
        viewModel.postInfoLiveData.observe(viewLifecycleOwner) { post ->
            binding.kaspi.text = post.telDonation
            binding.card.text = post.cardDonation
        }
        viewModel.getPostInfo(postId)

    }
}
