package com.example.tanu.ui.main

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.tanu.SessionManager
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.data.retrofit.ApiClient
import com.example.tanu.databinding.ActivityMediaUploadBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


class MediaUploadActivity : AppCompatActivity() {
    private lateinit var viewModel: MediaUploadViewModel
    lateinit var binding: ActivityMediaUploadBinding
    private var selectedMediaUris: MutableList<Uri> = mutableListOf()
    lateinit var mediaLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val apiClient = ApiClient()
        val sessionManager = SessionManager(this)
        val repository = MainRepository(apiClient.getApiService(this), sessionManager)
        viewModel = ViewModelProvider(this, MediaUploadViewModelFactory(repository)).get(MediaUploadViewModel::class.java)
        mediaLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    val clipData = result.data?.clipData
                    if (clipData != null) {
                        for (i in 0 until clipData.itemCount) {
                            val uri = clipData.getItemAt(i).uri
                            selectedMediaUris.add(uri)
                        }
                    } else {
                        val selectedMediaUri = result.data?.data
                        selectedMediaUri?.let {
                            selectedMediaUris.add(it)
                        }
                    }
                    showPostView()
                }
            }
        binding.uploadView.setOnClickListener {
            checkPermissionAndOpenMediaPicker()
        }

        binding.submitPostBtn.setOnClickListener {
            requestUploadSurvey()

        }

        binding.cancelPostBtn.setOnClickListener {
            finish()
        }
        viewModel.uploadStatus.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Media upload successful", Toast.LENGTH_SHORT).show()
                finish() // Close the activity after successful upload
            } else {
                Toast.makeText(this, "Failed to upload media", Toast.LENGTH_SHORT).show()
            }
            setInProgress(false) // Hide progress bar after upload attempt
        })
    }
    @SuppressLint("Range")
    private fun getFileName(uri: Uri): String? {
        var fileName: String? = null
        try {
            val cursor = contentResolver.query(uri, null, null, null, null)
            cursor?.use {
                if (it.moveToFirst()) {
                    fileName = it.getString(it.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error retrieving file name: ${e.message}")
        }
        return fileName
    }
    private fun requestUploadSurvey() {
        Log.e(TAG, "requestUploadSurvey called")
        val path = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val filesParts = arrayOfNulls<MultipartBody.Part>(selectedMediaUris.size)
        for (index in 0 until selectedMediaUris.size) {
            val uri = selectedMediaUris[index]
            val fileName = getFileName(uri)
            Log.e(TAG, fileName!!)
            val file = File(path,fileName ?: "")
            val filesBody = file.asRequestBody("image/* video/*".toMediaTypeOrNull())
            filesParts[index] = MultipartBody.Part.createFormData(
                "files",
                file.name,
                filesBody
            )
        }

        // Call the ViewModel method to upload survey
        viewModel.postPost(filesParts, "укпукпкупу")
    }
    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.submitPostBtn.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.submitPostBtn.visibility = View.VISIBLE
        }
    }

    private fun showPostView() {
        binding.postView.visibility = View.VISIBLE
        binding.uploadView.visibility = View.GONE
        if (selectedMediaUris.isNotEmpty()) {
            Glide.with(binding.postThumbnailView).load(selectedMediaUris[0])
                .into(binding.postThumbnailView)
        }
    }
    private fun getRealPathFromURI(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        val idx = cursor?.getColumnIndex("_data")
        val path = cursor?.getString(idx ?: 0)
        cursor?.close()
        return path ?: ""
    }
    private fun checkPermissionAndOpenMediaPicker() {
        val readExternalStoragePermission = android.Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(
                this,
                readExternalStoragePermission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // We have permission
            openMediaPicker()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(readExternalStoragePermission),
                100
            )
        }
    }
    private fun postMedia() {
        if (binding.postCaptionInput.text.toString().isEmpty()) {
            binding.postCaptionInput.setError("Write something")
            return
        }
        setInProgress(true)

    }

    private fun openMediaPicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/* video/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        mediaLauncher.launch(intent)
    }
}