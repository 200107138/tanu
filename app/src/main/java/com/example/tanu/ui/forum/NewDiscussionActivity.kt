// NewDiscussionActivity.kt
package com.example.tanu.ui.forum

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.databinding.ActivityNewDiscussionBinding
import com.example.tanu.SessionManager
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient

class NewDiscussionActivity : AppCompatActivity() {

    private lateinit var viewModel: NewDiscussionViewModel
    private lateinit var binding: ActivityNewDiscussionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewDiscussionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiClient = ApiClient()
        val sessionManager = SessionManager(this)
        val repository = MainRepository(apiClient.getApiService(this), sessionManager)
        val factory = NewDiscussionViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(NewDiscussionViewModel::class.java)

        val postId = intent.getStringExtra("postId") ?: ""
        binding.submitButton.setOnClickListener {
            val title = binding.titleEditText.text.toString()
            val text = binding.textEditText.text.toString()
            viewModel.postDiscussion(title, text, postId)
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
