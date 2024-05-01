package com.example.tanu.ui.main
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.R
import com.example.tanu.SessionManager
import com.example.tanu.data.adapters.MediaListAdapter
import com.example.tanu.data.adapters.PostFragmentAdapter
import com.example.tanu.data.adapters.PostMediaAdapter
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.ActivityPostBinding
import com.google.android.material.tabs.TabLayoutMediator

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    private lateinit var viewModel: PostViewModel
    private lateinit var postId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiClient = ApiClient()
        val sessionManager = SessionManager(this)
        val repository = MainRepository(apiClient.getApiService(this), sessionManager)
        viewModel = ViewModelProvider(this, PostViewModelFactory(repository)).get(PostViewModel::class.java)

        // Retrieve postId from intent extras
        postId = intent.getStringExtra("postId") ?: return
        viewModel.getPostInfo(postId)
        binding.back.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        // Observe postInfoLiveData
        viewModel.postInfoLiveData.observe(this, Observer { post ->
            post?.let {
                // Set title
                binding.postTitle.text = it.title


                // Set up ViewPager2 with MediaListAdapter
                val mediaListAdapter = PostMediaAdapter(it.mediaUrls)
                binding.mediaViewPager.adapter = mediaListAdapter
                when (it.rating) {
                    in 4.1..5.0 -> {
                        val backgroundColor = ContextCompat.getColor(this, R.color.rating_five) // Get color from resources
                        binding.ratingLayout.backgroundTintList = ColorStateList.valueOf(backgroundColor) // Set background tint
                        binding.ratingImage.setImageResource(R.drawable.five)
                        binding.rating.text =
                            Html.fromHtml("<b>${it.rating}</b> This user has an average rating of ${it.rating}")
                    }

                    in 3.1..4.0 -> {
                        binding.ratingImage.setImageResource(R.drawable.four)
                        val backgroundColor = ContextCompat.getColor(this, R.color.rating_four) // Get color from resources
                        binding.ratingLayout.backgroundTintList = ColorStateList.valueOf(backgroundColor) // Set background tint
                        binding.rating.text =
                            Html.fromHtml("<b>${it.rating}</b> This user has an average rating of ${it.rating}")
                    }

                    in 2.1..3.0 -> {
                        binding.ratingImage.setImageResource(R.drawable.three)
                        val backgroundColor = ContextCompat.getColor(this, R.color.rating_three) // Get color from resources
                        binding.ratingLayout.backgroundTintList = ColorStateList.valueOf(backgroundColor) // Set background tint
                        binding.rating.text =
                            Html.fromHtml("<b>${it.rating}</b> This user has an average rating of ${it.rating}")
                    }

                    in 1.1..2.0 -> {
                        binding.ratingImage.setImageResource(R.drawable.two)
                        val backgroundColor = ContextCompat.getColor(this, R.color.rating_two) // Get color from resources
                        binding.ratingLayout.backgroundTintList = ColorStateList.valueOf(backgroundColor) // Set background tint
                        binding.rating.text =
                            Html.fromHtml("<b>${it.rating}</b> This user has an average rating of ${it.rating}")
                    }

                    in 0.0..1.0 -> {
                        binding.ratingImage.setImageResource(R.drawable.one)
                        val backgroundColor = ContextCompat.getColor(this, R.color.rating_one) // Get color from resources
                        binding.ratingLayout.backgroundTintList = ColorStateList.valueOf(backgroundColor) // Set background tint
                        binding.rating.text =
                            Html.fromHtml("<b>${it.rating}</b> This user has an average rating of ${it.rating}")
                    }

                    else -> {
                        // Handle any other cases if needed
                    }
                }
            }
        })

        // Set up ViewPager2 with TabLayout
        val fragments = listOf(
            PostDescriptionFragment.newInstance(postId),
            PostDiscussionFragment.newInstance(postId),
            PostRateFragment.newInstance(postId),
            PostDonationFragment.newInstance(postId)
        )
        val adapter = PostFragmentAdapter(this, fragments)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Сипаттама"
                1 -> "Форум"
                2 -> "Багалау"
                3 -> "Кайырымдылык"
                else -> ""
            }
        }.attach()
    }
}
