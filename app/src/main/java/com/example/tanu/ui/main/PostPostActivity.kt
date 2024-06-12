package com.example.tanu.ui.main


import android.R
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.tanu.SessionManager
import com.example.tanu.data.adapters.MediaListAdapter
import com.example.tanu.data.models.PostCategory
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.ActivityPostPostBinding
import com.google.android.material.textfield.MaterialAutoCompleteTextView

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.util.*

class PostPostActivity : AppCompatActivity() {
    private lateinit var viewModel: PostPostViewModel
    lateinit var binding: ActivityPostPostBinding
    private val images = ArrayList<Uri>()
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private var categoryList = listOf<PostCategory>()
    private lateinit var mediaAdapter: MediaListAdapter
    private var selectedCategoryId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiClient = ApiClient()
        val sessionManager = SessionManager(this)
        val repository = MainRepository(apiClient.getApiService(this), sessionManager)
        viewModel = ViewModelProvider(this, PostPostViewModelFactory(repository)).get(PostPostViewModel::class.java)
        viewModel.uploadStatus.observe(this) { uploading ->
            if (uploading) {
                // If uploading is true, finish this activity and start SuccessPostPostActivity
                val intent = Intent(this, SuccessPostPostActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // If uploading is false, enable the button
                binding.btnSubmit.isEnabled = true
            }
        }
        viewModel.getPostCategories()

        viewModel.postCategoriesLiveData.observe(this) { categories ->
            categoryList = categories // Store the list of categories
            setCategoryDropdown(categories.map { it.name }) // Pass the list of category names to set up the dropdown
        }

        binding.uploadMediaLayout.setOnClickListener {
            openPhotoPicker()
        }
        binding.close.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        // Initialize ActivityResultLauncher
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val data = result.data
                if (data?.clipData != null) {
                    val count = data.clipData!!.itemCount
                    for (i in 0 until count) {
                        val imageUri = data.clipData!!.getItemAt(i).uri
                        images.add(imageUri)
                    } } else if (data?.data != null) { // Single image selected
                    val imageUri = data.data
                    images.add(imageUri!!)
                }
            }
            showPostView()
        }
        binding.btnSubmit.setOnClickListener {
            binding.btnSubmit.isEnabled = false
            uploadToServer()
        }
    }

    private fun openPhotoPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        galleryLauncher.launch(intent)
    }

    private fun showPostView() {
        binding.mediaListLayout.visibility = View.VISIBLE
        binding.uploadMediaLayout.visibility = View.GONE
        if (images.isNotEmpty()) {
            mediaAdapter = MediaListAdapter(images)
            binding.mediaList.apply {
                layoutManager = LinearLayoutManager(this@PostPostActivity, LinearLayoutManager.HORIZONTAL, false)
                adapter = mediaAdapter
            }
        }
    }


    private fun uploadToServer() {
        binding.btnSubmit.isEnabled = false
        val description = binding.description.text.toString()
        val title = binding.title.text.toString()
        val telDonation = binding.telDonation.text.toString()
        val cardDonation = binding.cardDonation.text.toString()
        val list: MutableList<MultipartBody.Part> = ArrayList()
        for (uri in images) {
            list.add(prepairFiles("files", uri))
        }

        if (list.isNotEmpty() && description.isNotEmpty() && title.isNotEmpty() && telDonation.isNotEmpty() && cardDonation.isNotEmpty() && selectedCategoryId != null && images.isNotEmpty()) {
                viewModel.postPost(
                    files = list.toTypedArray(),
                    description = description,
                    title = title,
                    telDonation = telDonation.toLong(),
                    cardDonation = cardDonation.toLong(),
                    categoryId = selectedCategoryId!!
                )
        } else {
            // Show error message for missing fields
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }
    private fun prepairFiles(partName: String, fileUri: Uri): MultipartBody.Part {
        val contentResolver = contentResolver
        val inputStream = contentResolver.openInputStream(fileUri)
        val file = File(getFileNameFromUri(fileUri)!!) // Assuming you have a function to get file name from URI
        val requestBody = inputStream?.readBytes()?.toRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestBody!!)
    }
    private fun getFileNameFromUri(uri: Uri): String? {
        var fileName: String? = null
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val displayNameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (displayNameIndex != -1) {
                    fileName = it.getString(displayNameIndex)
                }
            }
        }
        return fileName
    }
    private fun setCategoryDropdown(categories: List<String>) {
        val adapter = ArrayAdapter(this, R.layout.simple_dropdown_item_1line, categories)
        val categoryDropdown: AutoCompleteTextView = binding.categoryDropdown
        categoryDropdown.setAdapter(adapter)

        // Set a click listener to handle item selection
        categoryDropdown.setOnItemClickListener { _, _, position, _ ->
            selectedCategoryId = categoryList[position].id // Save selectedCategoryId
        }
    }

}