package com.example.tanu.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

class PostDonationViewModel(private val repository: MainRepository) : ViewModel() {
    private val _postInfoLiveData = MutableLiveData<Post>()
    val postInfoLiveData: LiveData<Post> = _postInfoLiveData

    fun getPostInfo(postId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getPostInfo(postId)
                if (response.isSuccessful) {
                    _postInfoLiveData.postValue(response.body()?.post)
                } else {
                    // Handle unsuccessful response
                    Log.e("MessageViewModel", "Unsuccessful response: ${response.code()}")
                }
            } catch (e: Exception) {
                // Handle error
                Log.e("MessageViewModel", "Error getting post info: ${e.message}")
            }
        }
    }

}