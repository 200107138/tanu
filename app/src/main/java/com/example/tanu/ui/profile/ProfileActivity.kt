package com.example.tanu.ui.profile

import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.tanu.R
import com.example.tanu.SessionManager
import com.example.tanu.data.UserHolder
import com.example.tanu.data.adapters.PostListAdapter
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.ActivityProfileBinding

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var viewModel: ProfileViewModel
    private lateinit var profilePostAdapter: PostListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get userId from arguments
        val userId = intent.getStringExtra("userId") ?: ""

        // Initialize ViewModel
        val apiClient = ApiClient()
        val sessionManager = SessionManager(this)
        val repository = MainRepository(apiClient.getApiService(this), sessionManager)
        viewModel = ViewModelProvider(this, ProfileViewModelFactory(repository)).get(
            ProfileViewModel::class.java)

        // Initialize RecyclerView adapter
        profilePostAdapter = PostListAdapter(this)

        // Set RecyclerView adapter
        binding.recyclerView.adapter = profilePostAdapter

        // Set GridLayoutManager with spanCount 3
        binding.recyclerView.layoutManager = GridLayoutManager(this, 3)

        // Observe postsLiveData
        viewModel.postsLiveData.observe(this, Observer { posts ->
            posts?.let {
                // Update RecyclerView with the list of posts
                profilePostAdapter.submitList(posts)
            }
        })


        viewModel.userInfoLiveData.observe(this, Observer { userInfo ->
            userInfo?.let {
                // Update UI with user information
                if(userInfo.user.avatarUrl != null){
                    Glide.with(this)
                        .load(userInfo.user.avatarUrl)
                        .into(binding.avatar)
                }
                binding.email.text = userInfo.user.email
                binding.name.text = userInfo.user.name
                when (userInfo.user.rating) {
                    in 81.0..100.0 -> {
                        binding.ratingImage.setImageResource(R.drawable.five)
                        binding.ratingLayout.setBackgroundColor(resources.getColor(R.color.rating_five))
                        binding.rating.text =
                            Html.fromHtml("<b>Great:</b> This user has an average rating of ${userInfo.user.rating}")
                    }

                    in 61.0..80.0 -> {
                        binding.ratingImage.setImageResource(R.drawable.four)
                        binding.ratingLayout.setBackgroundColor(resources.getColor(R.color.rating_four))
                        binding.rating.text =
                            Html.fromHtml("<b>Good:</b> This user has an average rating of ${userInfo.user.rating}")
                    }

                    in 41.0..60.0 -> {
                        binding.ratingImage.setImageResource(R.drawable.three)
                        binding.ratingLayout.setBackgroundColor(resources.getColor(R.color.rating_three))
                        binding.rating.text =
                            Html.fromHtml("<b>Normal:</b> This user has an average rating of ${userInfo.user.rating}")
                    }

                    in 21.0..40.0 -> {
                        binding.ratingImage.setImageResource(R.drawable.two)
                        binding.ratingLayout.setBackgroundColor(resources.getColor(R.color.rating_two))
                        binding.rating.text =
                            Html.fromHtml("<b>Bad:</b> This user has an average rating of ${userInfo.user.rating}")
                    }

                    in 0.0..20.0 -> {
                        binding.ratingImage.setImageResource(R.drawable.one)
                        binding.ratingLayout.setBackgroundColor(resources.getColor(R.color.rating_one))
                        binding.rating.text =
                            Html.fromHtml("<b>Bad:</b> This user has an average rating of ${userInfo.user.rating}")
                    }

                    else -> {
                        // Handle any other cases if needed
                    }
                }
            }
        })



        // Call getPostsByUserId API with the user ID
        viewModel.getPostsByUserId(userId)
        viewModel.getUserInfo(userId)

    }
}
