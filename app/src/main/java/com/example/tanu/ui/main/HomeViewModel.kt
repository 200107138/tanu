package com.example.tanu.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class HomeViewModel(private val repository: MainRepository) : ViewModel() {
    private val _postsLiveData: MutableLiveData<List<Post>> = MutableLiveData()
    val postsLiveData: LiveData<List<Post>> = _postsLiveData

    fun getPosts() {
        viewModelScope.launch {
            try {
                val response = repository.getPosts()
                if (response?.status == "success") {
                    _postsLiveData.postValue(response.posts)
                } else {
                    // Handle error or empty response
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }

}
