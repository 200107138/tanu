package com.example.tanu.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.GetReviewByPostIdAndUserIdResponse
import com.example.tanu.data.models.PostCommentRequest
import com.example.tanu.data.models.PostRateRequest
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

class PostRateViewModel(private val repository: MainRepository) : ViewModel() {
    private val _postCommentLiveData = MutableLiveData<String>()
    val postCommentLiveData: LiveData<String>
        get() = _postCommentLiveData
    val commentLiveData: MutableLiveData<GetReviewByPostIdAndUserIdResponse?> = MutableLiveData()
    fun postComment(postId: String, commentText: String) {
        viewModelScope.launch {
            try {
                val response = repository.postComment(PostCommentRequest(postId, commentText))
                if (response?.status == "success") {
                    _postCommentLiveData.value = "success"
                } else {
                    _postCommentLiveData.value = "error"
                }
            } catch (e: Exception) {
                _postCommentLiveData.value = "error"
            }
        }
    }
    fun getReviewByPostIdAndUserId(postId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getReviewByPostIdAndUserId(postId)

                if (response?.status == "success") {
                    commentLiveData.postValue(response)
                } else {
                    // Handle error or empty response
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }
    fun postRate(postId: String, rate: Int) {
        viewModelScope.launch {
            try {
                val response = repository.postRate(PostRateRequest(postId, rate))
                if (response?.status == "success") {

                } else {
                    // Handle error or empty response
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }

}