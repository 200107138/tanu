// NewDiscussionActivity.kt
package com.example.tanu.ui.forum

import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.databinding.ActivityNewDiscussionBinding
import com.example.tanu.SessionManager
import com.example.tanu.data.models.DiscussionCategory
import com.example.tanu.data.models.PostCategory
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient

class NewDiscussionActivity : AppCompatActivity() {

    private lateinit var viewModel: NewDiscussionViewModel
    private lateinit var binding: ActivityNewDiscussionBinding
    private var categoryList = listOf<DiscussionCategory>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewDiscussionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.close.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        val apiClient = ApiClient()
        val sessionManager = SessionManager(this)
        val repository = MainRepository(apiClient.getApiService(this), sessionManager)
        val factory = NewDiscussionViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(NewDiscussionViewModel::class.java)
        viewModel.getDiscussionCategories()

        viewModel.discussionCategoriesLiveData.observe(this) { categories ->
            categoryList = categories // Store the list of categories
            setCategoryDropdown(categories.map { it.name }) // Pass the list of category names to set up the dropdown
        }


        viewModel.postDiscussionLiveData.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                binding.submitButton.isEnabled = false
                Toast.makeText(this, "Discussion posted successfully", Toast.LENGTH_SHORT).show()
                finish() // Close the activity
            } else {
                binding.submitButton.isEnabled = true
                Toast.makeText(this, "Failed to post discussion", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun setCategoryDropdown(categories: List<String>) {
        val adapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, categories)
        val categoryDropdown: AutoCompleteTextView = binding.categoryDropdown
        categoryDropdown.setAdapter(adapter)

        // Set a click listener to handle item selection
        categoryDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedCategory = categoryList[position] // Get the selected category object
            val categoryId = selectedCategory.id // Get the ID of the selected category
            // Call postDiscussion function with the selected category ID
            binding.submitButton.setOnClickListener {
                binding.submitButton.isEnabled = false
                val title = binding.title.text.toString()
                val description = binding.description.text.toString()
                viewModel.postDiscussion(title, description, categoryId)
            }
        }
    }

}
