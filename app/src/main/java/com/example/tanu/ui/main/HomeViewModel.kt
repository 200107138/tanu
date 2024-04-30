package com.example.tanu.ui.main

import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.ChatRoom
import com.example.tanu.data.models.Post
import com.example.tanu.data.models.PostCategory
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class HomeViewModel(private val repository: MainRepository) : ViewModel() {
    private val _postsLiveData: MutableLiveData<List<Post>> = MutableLiveData()
    val postsLiveData: LiveData<List<Post>> = _postsLiveData

    private val _postCategoriesLiveData: MutableLiveData<List<PostCategory>> = MutableLiveData()
    val postCategoriesLiveData: LiveData<List<PostCategory>> = _postCategoriesLiveData

    private val _postsRatedLiveData: MutableLiveData<Int> = MutableLiveData()
    val postsRatedLiveData: LiveData<Int> = _postsRatedLiveData
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

    fun getPostsRated() {
        viewModelScope.launch {
            try {
                val response = repository.getPostsRated()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _postsRatedLiveData.postValue(it.postsRated)
                    }
                } else {

                }
            } catch (e: Exception) {

            }
        }
    }
}
