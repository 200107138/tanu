package com.example.tanu.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.tanu.R
import com.example.tanu.data.adapters.PostFragmentAdapter
import com.example.tanu.data.models.Post
import com.example.tanu.databinding.ActivityPostBinding
import com.google.android.material.tabs.TabLayoutMediator

class PostActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPostBinding
    private lateinit var post: Post

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve post object from intent extras
        post = intent.getSerializableExtra("post") as Post

        // Set up ViewPager2 with TabLayout
        val fragments = listOf(
            PostDescriptionFragment.newInstance(post),
            PostDiscussionFragment.newInstance(post),
            PostRateFragment.newInstance(post)
        )
        val adapter = PostFragmentAdapter(this, fragments)
        binding.viewPager.adapter = adapter
        binding.viewPager.isUserInputEnabled = false
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Сипаттама"
                1 -> "Форум"
                2 -> "Бағалау"
                else -> ""
            }
        }.attach()
        binding.tabLayout.getTabAt(0)!!.setIcon(R.drawable.outline_description_24)
        binding.tabLayout.getTabAt(1)!!.setIcon(R.drawable.outline_forum_24)
        binding.tabLayout.getTabAt(2)!!.setIcon(R.drawable.baseline_star_outline_24)
    }
}
