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
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.FragmentPostDiscussionBinding
import com.example.tanu.ui.forum.DiscussionActivity
import com.example.tanu.ui.forum.NewDiscussionActivity

class PostDiscussionFragment : Fragment() {

    private lateinit var viewModel: PostDiscussionViewModel
    private lateinit var binding: FragmentPostDiscussionBinding
    private lateinit var adapter: DiscussionListAdapter

    companion object {
        private const val POST_ID_KEY = "postId"

        fun newInstance(postId: String): PostDiscussionFragment {
            val fragment = PostDiscussionFragment()
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
        binding = FragmentPostDiscussionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val postId = arguments?.getString("postId") ?: ""

        val apiClient = ApiClient()
        val sessionManager = SessionManager(requireContext())
        val repository = MainRepository(apiClient.getApiService(requireContext()), sessionManager)
        viewModel = ViewModelProvider(this, PostDiscussionViewModelFactory(repository)).get(
            PostDiscussionViewModel::class.java)
        adapter = DiscussionListAdapter(requireContext())
        // Set RecyclerView adapter
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe discussionsLiveData
        viewModel.discussionsLiveData.observe(viewLifecycleOwner, Observer { discussions ->
            discussions?.let {
                adapter.setDiscussions(discussions)
            }
        })

        // Call getDiscussionsByPostId API
        viewModel.getDiscussionsByPostId(postId)

        // Add click listener to submitButton to open NewDiscussionActivity and pass postId into it
        binding.submitButton.setOnClickListener {
            val intent = Intent(requireContext(), NewPostDiscussionActivity::class.java)
            intent.putExtra("postId", postId)
            startActivity(intent)
        }
    }
}
