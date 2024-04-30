package com.example.tanu.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.PostRateRequest
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

class PostRateViewModel(private val repository: MainRepository) : ViewModel() {

    private val _postRatingLiveData: MutableLiveData<Int> =
        MutableLiveData()
    val postRatingLiveData: LiveData<Int> =
        _postRatingLiveData

    fun getPostRating(postId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getPostRating(postId)
                if (response.isSuccessful) {
                    _postRatingLiveData.postValue(response.body()?.rate)
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