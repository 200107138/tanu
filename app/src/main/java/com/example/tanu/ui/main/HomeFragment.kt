package com.example.tanu.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DefaultItemAnimator
import com.example.tanu.data.Adapters.PostsAdapter
import com.example.tanu.data.Models.Post
import com.example.tanu.R
import com.example.tanu.SessionManager
import com.example.tanu.data.Repository.MainRepository
import com.example.tanu.data.Retrofit.ApiClient
import com.example.tanu.databinding.FragmentHomeBinding
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class HomeFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var postsAdapter: PostsAdapter
    private lateinit var manager: CardStackLayoutManager

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
    }

    private fun initCardStackView(posts: ArrayList<Post>) {
        manager = CardStackLayoutManager(requireContext(), object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {}
            override fun onCardSwiped(direction: Direction?) {}
            override fun onCardRewound() {}
            override fun onCardCanceled() {}
            override fun onCardAppeared(view: View?, position: Int) {}
            override fun onCardDisappeared(view: View?, position: Int) {}
        })
        manager.setVisibleCount(3)
        manager.setTranslationInterval(0.6f)
        manager.setScaleInterval(0.8f)
        manager.setMaxDegree(20.0f)
        manager.setDirections(Direction.HORIZONTAL)
        postsAdapter = PostsAdapter(requireContext(), posts)
        binding.cardStackView.apply {
            layoutManager = manager
            itemAnimator = DefaultItemAnimator()
            adapter = postsAdapter
        }
    }
}
