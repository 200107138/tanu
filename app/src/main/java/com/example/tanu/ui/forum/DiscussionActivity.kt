// DiscussionActivity.kt
package com.example.tanu.ui.forum

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tanu.SessionManager
import com.example.tanu.data.adapters.DiscussionCommentAdapter
import com.example.tanu.data.models.Discussion
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.ActivityDiscussionBinding

class DiscussionActivity : AppCompatActivity() {

    private lateinit var viewModel: DiscussionViewModel
    private lateinit var binding: ActivityDiscussionBinding
    private lateinit var adapter: DiscussionCommentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDiscussionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val discussionId = intent.getStringExtra("discussionId") ?: ""
        val apiClient = ApiClient()
        val sessionManager = SessionManager(this)
        val repository = MainRepository(apiClient.getApiService(this), sessionManager)
        viewModel = ViewModelProvider(this, DiscussionViewModelFactory(repository)).get(DiscussionViewModel::class.java)

        adapter = DiscussionCommentAdapter()

        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        viewModel.postCommentStatus.observe(this, Observer { success ->
            if (success) {
                viewModel.getDiscussionCommentsById(discussionId)
            } else {
                // Handle error if needed
            }
        })
        viewModel.discussionCommentsLiveData.observe(this, Observer { discussionComments ->
            discussionComments?.let {
                adapter.setDiscussionComments(discussionComments)
            }
        })

        viewModel.getDiscussionCommentsById(discussionId)

        viewModel.getDiscussionInfo(discussionId)
        viewModel.discussionInfoLiveData.observe(this, Observer { discussion ->
            discussion?.let {
                binding.discussionTitle.text = discussion.title
                binding.discussionDesc.text = discussion.description
                binding.name.text = discussion.user.name
                if(discussion.user.avatarUrl != null){
                    Glide.with(binding.root)
                        .load(discussion.user.avatarUrl)
                        .into(binding.userAvatar)
                }
            }
        })



        // Set up button click listener to add a new comment
        binding.button.setOnClickListener {
            val commentText = binding.editText.text.toString().trim()
            if (commentText.isNotEmpty()) {
                viewModel.postDiscussionComment(discussionId, commentText)
                binding.editText.text?.clear() // Clear the EditText after adding the comment
            } else {
                Toast.makeText(this, "Please enter a comment", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
