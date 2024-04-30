package com.example.tanu.ui.forum

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tanu.SessionManager
import com.example.tanu.data.adapters.DiscussionCategoryAdapter
import com.example.tanu.data.adapters.DiscussionListAdapter
import com.example.tanu.data.adapters.PostCategoryAdapter
import com.example.tanu.data.models.Discussion
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.FragmentForumBinding


class ForumFragment : Fragment() {

    private lateinit var viewModel: ForumViewModel
    private lateinit var binding: FragmentForumBinding
    private lateinit var adapter: DiscussionListAdapter
    private lateinit var categoryAdapter: DiscussionCategoryAdapter
    private var originalDiscussions: List<Discussion> = emptyList() // Store the original list of posts
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
        adapter = DiscussionListAdapter(requireContext())
        // Set RecyclerView adapter
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        categoryAdapter = DiscussionCategoryAdapter(requireContext()) { categoryStates ->
            filterPostsByCategory(categoryStates)
        }
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoryRecyclerView.adapter = categoryAdapter
        viewModel.discussionCategoriesLiveData.observe(viewLifecycleOwner, Observer { categories ->
            categories?.let {
                categoryAdapter.submitList(categories)
            }
        })

        // Observe discussionsLiveData
        viewModel.discussionsLiveData.observe(viewLifecycleOwner, Observer { discussions ->
            discussions?.let {
                originalDiscussions = it
                adapter.setDiscussions(discussions)
            }
        })

        // Call getAllDiscussion API
        viewModel.getAllDiscussion()
        viewModel.getDiscussionCategories()
        // Set up SearchView listener
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    filterDiscussions(it) // Filter posts when the text changes
                }
                return false
            }
        })
        binding.buttonNewDiscussion.setOnClickListener {
            val intent = Intent(requireContext(), NewDiscussionActivity::class.java)
            startActivity(intent)
        }
    }
    private fun filterDiscussions(query: String) {
        val filteredDiscussions = originalDiscussions.filter { discussion ->
            discussion.title?.contains(query, ignoreCase = true) ?: false ||
                    discussion.description?.contains(query, ignoreCase = true) ?: false
        }
        adapter.submitList(filteredDiscussions)
    }

    private fun filterPostsByCategory(categoryStates: Map<String, Boolean>) {
        val allCategoriesDisabled = categoryStates.all { it.value == false }
        val filteredDiscussion = if (allCategoriesDisabled) {
            originalDiscussions // If all categories are disabled, submit the original list
        } else {
            originalDiscussions.filter { post ->
                val categoryId = post.categoryId
                categoryStates[categoryId] ?: false // Get the state of the category; default is false if not found
            }
        }
        adapter.submitList(filteredDiscussion)
    }
}
