package com.example.tanu.ui.main

import android.content.ContentValues
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.PostCategory
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class PostPostViewModel(private val repository: MainRepository) : ViewModel() {
    private val _postCategoriesLiveData: MutableLiveData<List<PostCategory>> = MutableLiveData()
    val postCategoriesLiveData: LiveData<List<PostCategory>> = _postCategoriesLiveData
    private val _uploadStatus = MutableLiveData<Boolean>()
    val uploadStatus: LiveData<Boolean>
        get() = _uploadStatus


    fun postPost(mediaParts: Array<MultipartBody.Part?>, description: String, title: String, telDonation: Long, cardDonation: Long) {
        Log.e(ContentValues.TAG, "uploadPost apiviewmodel called")
        viewModelScope.launch {
            try {
                val response = repository.postPost(mediaParts, description,title,telDonation,cardDonation)
                Log.d(ContentValues.TAG, "PostPost API response status: ${response?.status}")
            } catch (e: Exception) {
                // Handle exceptions here if needed
                Log.e(ContentValues.TAG, "Error posting post: ${e.message}")
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