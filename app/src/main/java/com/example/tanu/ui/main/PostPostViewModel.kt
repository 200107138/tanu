package com.example.tanu.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.PostCategory
import com.example.tanu.data.models.PostPostRequest
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class PostPostViewModel(private val repository: MainRepository) : ViewModel() {
    private val _postCategoriesLiveData: MutableLiveData<List<PostCategory>> = MutableLiveData()
    val postCategoriesLiveData: LiveData<List<PostCategory>> = _postCategoriesLiveData
    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: LiveData<Boolean>
        get() = _uploadStatus


    fun postPost(files: Array<MultipartBody.Part?>, description: String, title: String, telDonation: Long, cardDonation: Long, categoryId: String) {
        viewModelScope.launch {
            try {
                val response = repository.postPost(files = files, description = description, title = title, telDonation = telDonation, cardDonation = cardDonation, categoryId = categoryId)// Upload failed
                // Upload successful
                _uploadStatus.value = response!!.status == "success"
            } catch (e: Exception) {
                // Handle exceptions here if needed
                Log.e(ContentValues.TAG, "Error posting post: ${e.message}")
                _uploadStatus.postValue(true) // Set upload status to true on error
            }
        }
    }
    fun getPostCategories() {
        viewModelScope.launch {
            try {
                val response = repository.getPostCategories()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _postCategoriesLiveData.postValue(it.categories)
                    }
                } else {

                }
            } catch (e: Exception) {

            }
        }
    }
}