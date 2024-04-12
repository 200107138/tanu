package com.example.tanu.ui.forum

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tanu.SessionManager
import com.example.tanu.data.adapters.DiscussionAdapter
import com.example.tanu.data.models.Discussion
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.FragmentForumBinding


class ForumFragment : Fragment() {

    private lateinit var viewModel: ForumViewModel
    private lateinit var binding: FragmentForumBinding
    private lateinit var adapter: DiscussionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForumBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val apiClient = ApiClient()
        val sessionManager = SessionManager(requireContext())
        val repository = MainRepository(apiClient.getApiService(requireContext()), sessionManager)
        viewModel = ViewModelProvider(this, ForumViewModelFactory(repository)).get(
            ForumViewModel::class.java)
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

        // Call getAllDiscussion API
        viewModel.getAllDiscussion()
        binding.buttonNewDiscussion.setOnClickListener {
            val intent = Intent(requireContext(), NewDiscussionActivity::class.java)
            startActivity(intent)
        }
    }
}
