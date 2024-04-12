package com.example.tanu.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

class LeaderboardViewModel(private val repository: MainRepository) : ViewModel() {

    private val _leaderboardPosts = MutableLiveData<List<Post>>()
    val leaderboardPosts: LiveData<List<Post>> get() = _leaderboardPosts

    fun getLeaderboardPosts() {
        viewModelScope.launch {
            try {
                val response = repository.getLeaderboardPosts()
                if (response!!.status == "success") {
                    _leaderboardPosts.postValue(response.posts)
                } else {
                    // Handle error
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }
}
