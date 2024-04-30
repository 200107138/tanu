package com.example.tanu.ui.main
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tanu.SessionManager
import com.example.tanu.data.UserHolder
import com.example.tanu.data.adapters.PostCategoryAdapter
import com.example.tanu.data.adapters.PostListAdapter
import com.example.tanu.data.models.Post
import com.example.tanu.data.models.PostCategory
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var postAdapter: PostListAdapter
    private lateinit var categoryAdapter: PostCategoryAdapter
    private var originalPosts: List<Post> = emptyList() // Store the original list of posts

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel, Repository, and API client
        val apiClient = ApiClient()
        val sessionManager = SessionManager(requireContext())
        val repository = MainRepository(apiClient.getApiService(requireContext()), sessionManager)
        viewModel = ViewModelProvider(this, HomeViewModelFactory(repository)).get(HomeViewModel::class.java)
        viewModel.getPostsRated()

        // Observe postsRatedLiveData to update UI
        viewModel.postsRatedLiveData.observe(viewLifecycleOwner, Observer { postsRated ->
            // Update the UI with the number of posts rated
            binding.postsRated.text = "Рахмет! Сіз $postsRated жазбаларға баға бердіңіз."
        })
        // Initialize RecyclerView adapters
        postAdapter = PostListAdapter(requireContext())
        binding.posts.layoutManager = GridLayoutManager(requireContext(), 3)
        binding.posts.adapter = postAdapter
        categoryAdapter = PostCategoryAdapter(requireContext()) { categoryStates ->
            filterPostsByCategory(categoryStates)
        }
        binding.categoryRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.categoryRecyclerView.adapter = categoryAdapter



        viewModel.postsLiveData.observe(viewLifecycleOwner, Observer { posts ->
            posts?.let {
                originalPosts = posts // Save the original list of posts
                postAdapter.setPosts(posts)
            }
        })

        // Observe categoriesLiveData
        viewModel.postCategoriesLiveData.observe(viewLifecycleOwner, Observer { categories ->
            categories?.let {
                categoryAdapter.submitList(categories)
            }
        })

        // Call getPosts and getCategories APIs
        viewModel.getPosts()
        viewModel.getPostCategories()

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

    private fun filterPostsByCategory(categoryStates: Map<String, Boolean>) {
        val allCategoriesDisabled = categoryStates.all { it.value == false }
        val filteredPosts = if (allCategoriesDisabled) {
            originalPosts // If all categories are disabled, submit the original list
        } else {
            originalPosts.filter { post ->
                val categoryId = post.categoryId
                categoryStates[categoryId] ?: false // Get the state of the category; default is false if not found
            }
        }
        postAdapter.submitList(filteredPosts)
    }
}
