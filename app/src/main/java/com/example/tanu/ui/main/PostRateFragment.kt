// PostCommentFragment.kt
package com.example.tanu.ui.main

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.R
import com.example.tanu.SessionManager
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.FragmentPostRateBinding

class PostRateFragment : Fragment() {

    private lateinit var viewModel: PostRateViewModel
    private lateinit var binding: FragmentPostRateBinding
    private lateinit var post: Post
    private val ratingMap: HashMap<Int, Boolean> = HashMap()

    companion object {
        private const val POST_ID_KEY = "postId"

        fun newInstance(postId: String): PostRateFragment {
            val fragment = PostRateFragment()
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
        binding = FragmentPostRateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val postId = arguments?.getString(POST_ID_KEY) ?: ""

        val apiClient = ApiClient()
        val sessionManager = SessionManager(requireContext())
        val repository = MainRepository(apiClient.getApiService(requireContext()), sessionManager)
        viewModel = ViewModelProvider(this, PostRateViewModelFactory(repository)).get(
            PostRateViewModel::class.java)

        // Observe postRatingLiveData
        viewModel.postRatingLiveData.observe(viewLifecycleOwner, Observer { rating ->
            // Clear previous data
            ratingMap.clear()

            // Set values in ratingMap
            for (i in 1..5) {
                ratingMap[i] = rating == i
            }

            // Update UI based on ratingMap
            updateButtons()
        })

        // Get post rating
        viewModel.getPostRating(postId)

        // Set click listeners for rating buttons
        binding.apply {
            button1.setOnClickListener { onRatingButtonClicked(1) }
            button2.setOnClickListener { onRatingButtonClicked(2) }
            button3.setOnClickListener { onRatingButtonClicked(3) }
            button4.setOnClickListener { onRatingButtonClicked(4) }
            button5.setOnClickListener { onRatingButtonClicked(5) }
        }
    }

    private fun onRatingButtonClicked(rating: Int) {
        ratingMap.clear()

        // Set values in ratingMap
        for (i in 1..5) {
            ratingMap[i] = rating == i
        }
        updateButtons()
        val postId = arguments?.getString(POST_ID_KEY) ?: ""
        viewModel.postRate(postId, rating)
    }

    private fun updateButtons() {
        // Update button backgrounds based on ratingMap
        binding.apply {
            button1.backgroundTintList = getColorStateListForRating(1)
            button2.backgroundTintList = getColorStateListForRating(2)
            button3.backgroundTintList = getColorStateListForRating(3)
            button4.backgroundTintList = getColorStateListForRating(4)
            button5.backgroundTintList = getColorStateListForRating(5)
        }
    }

    private fun getColorStateListForRating(rating: Int): ColorStateList? {
        val colorResId = if (ratingMap[rating] == true) R.color.blue else R.color.gray
        return ContextCompat.getColorStateList(requireContext(), colorResId)
    }
}
