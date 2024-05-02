package com.example.tanu.ui.main


    import android.R
    import android.os.Bundle
    import android.widget.ArrayAdapter
    import android.widget.AutoCompleteTextView
    import android.widget.Toast
    import androidx.appcompat.app.AppCompatActivity
    import androidx.lifecycle.Observer
    import androidx.lifecycle.ViewModelProvider
    import com.bumptech.glide.Glide
    import com.example.tanu.databinding.ActivityNewDiscussionBinding
    import com.example.tanu.SessionManager
    import com.example.tanu.data.models.DiscussionCategory
    import com.example.tanu.data.models.PostCategory
    import com.example.tanu.data.repository.MainRepository
    import com.example.tanu.data.retrofit.ApiClient
    import com.example.tanu.databinding.ActivityNewPostDiscussionBinding

class NewPostDiscussionActivity : AppCompatActivity() {

        private lateinit var viewModel: NewPostDiscussionViewModel
        private lateinit var binding: ActivityNewPostDiscussionBinding
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityNewPostDiscussionBinding.inflate(layoutInflater)
            setContentView(binding.root)

            val apiClient = ApiClient()
            val sessionManager = SessionManager(this)
            val repository = MainRepository(apiClient.getApiService(this), sessionManager)
            val factory = NewPostDiscussionViewModelFactory(repository)
            viewModel = ViewModelProvider(this, factory).get(NewPostDiscussionViewModel::class.java)

            val postId = intent.getStringExtra("postId") ?: ""
            viewModel.getPostInfo(postId)
            viewModel.postInfoLiveData.observe(this, Observer { post ->
                post?.let {
                    binding.postTitle.text=post.title
                    if (it.mediaUrls.isNotEmpty()) {
                        Glide.with(this).load(it.mediaUrls[0]).into(binding.postMedia)
                    }
                }
            })
            binding.submitButton.setOnClickListener {
                binding.submitButton.isEnabled = false
                val title = binding.title.text.toString()
                val description = binding.description.text.toString()
                viewModel.postDiscussion(title, description, postId)
            }
            viewModel.postDiscussionLiveData.observe(this, Observer { isSuccess ->
                if (isSuccess) {
                    Toast.makeText(this, "Discussion posted successfully", Toast.LENGTH_SHORT).show()
                    finish() // Close the activity
                } else {
                    Toast.makeText(this, "Failed to post discussion", Toast.LENGTH_SHORT).show()
                }
            })
        }


    }
