package com.example.tanu.ui.main


import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.NonNull
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.tanu.SessionManager
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.ActivityPostPostBinding

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPostPostBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiClient = ApiClient()
        val sessionManager = SessionManager(this)
        val repository = MainRepository(apiClient.getApiService(this), sessionManager)
        viewModel = ViewModelProvider(this, PostPostViewModelFactory(repository)).get(PostPostViewModel::class.java)


        binding.uploadView.setOnClickListener {
            openPhotoPicker()
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
                    }
                } else if (data?.data != null) { // Single image selected
                    val imageUri = data.data
                    images.add(imageUri!!)
                }
            }
            showPostView()
        }
        binding.btnSubmit.setOnClickListener { uploadToServer() }
    }
    private fun openPhotoPicker() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        galleryLauncher.launch(intent)
    }

    private fun showPostView() {
        binding.postView.visibility = View.VISIBLE
        binding.uploadView.visibility = View.GONE
        if (images.isNotEmpty()) {
            Glide.with(binding.postThumbnailView).load(images[0])
                .into(binding.postThumbnailView)
        }
    }


    private fun uploadToServer() {
        // Convert Uri images to MultipartBody.Part
        val list: MutableList<MultipartBody.Part> = ArrayList()
        for (uri in images) {
            list.add(prepairFiles("files", uri))
        }

        // Call ViewModel function to upload images
        viewModel.postPost(list.toTypedArray(), "Post description")
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


}