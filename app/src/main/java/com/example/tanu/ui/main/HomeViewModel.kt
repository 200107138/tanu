package com.example.tanu.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.Models.Post
import com.example.tanu.data.Repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class HomeViewModel(private val repository: MainRepository) : ViewModel() {

    private val _postsLiveData: MutableLiveData<ArrayList<Post>> = MutableLiveData()
    val postsLiveData: LiveData<ArrayList<Post>> = _postsLiveData

    private val _errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val _commentStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val commentStatusLiveData: LiveData<Boolean> = _commentStatusLiveData

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getAllPosts()
                if (response.isSuccessful) {
                    _postsLiveData.postValue(response.body())
                } else {
                    _errorLiveData.postValue("Failed to get posts")
                }
            } catch (e: Exception) {
                _errorLiveData.postValue("Network error")
            }
        }
    }
    fun postComment(postId: String, commentText: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.postComment(postId, commentText)
                _commentStatusLiveData.postValue(true) // Comment posted successfully
            } catch (e: Exception) {
                _errorLiveData.postValue("Failed to post comment")
            }
        }
    }
}