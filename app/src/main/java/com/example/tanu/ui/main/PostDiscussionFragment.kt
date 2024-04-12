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
import com.example.tanu.data.adapters.DiscussionAdapter
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.FragmentPostDiscussionBinding
import com.example.tanu.ui.forum.DiscussionActivity
import com.example.tanu.ui.forum.NewDiscussionActivity

class PostDiscussionFragment : Fragment() {

    private lateinit var viewModel: PostDiscussionViewModel
    private lateinit var binding: FragmentPostDiscussionBinding
    private lateinit var adapter: DiscussionAdapter
    private lateinit var post: Post
    companion object {
        private const val POST_KEY = "post_key"

        fun newInstance(post: Post?): PostDiscussionFragment {
            val fragment = PostDiscussionFragment()
            val bundle = Bundle()
            bundle.putSerializable(POST_KEY, post)
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
        post = arguments?.getSerializable(POST_KEY) as? Post ?: return

        val apiClient = ApiClient()
        val sessionManager = SessionManager(requireContext())
        val repository = MainRepository(apiClient.getApiService(requireContext()), sessionManager)
        viewModel = ViewModelProvider(this, PostDiscussionViewModelFactory(repository)).get(
            PostDiscussionViewModel::class.java)

        // Initialize RecyclerView adapter with item click listener
        adapter = DiscussionAdapter { discussionId ->
            // Handle item click here, e.g., open DiscussionActivity
            // You can start an activity or perform any other action
            val intent = Intent(requireContext(), DiscussionActivity::class.java)
            intent.putExtra("discussionId", discussionId)
            startActivity(intent)
        }

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
        viewModel.getDiscussionsByPostId(post.id)

        // Add click listener to submitButton to open NewDiscussionActivity and pass postId into it
        binding.submitButton.setOnClickListener {
            val intent = Intent(requireContext(), NewDiscussionActivity::class.java)
            intent.putExtra("postId", post.id)
            startActivity(intent)
        }
    }
}
