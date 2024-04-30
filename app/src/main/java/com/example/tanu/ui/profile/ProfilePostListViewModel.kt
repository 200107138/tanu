package com.example.tanu.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.Post
import com.example.tanu.data.models.PutPostStatusRequest
import com.example.tanu.data.models.User
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

class ProfilePostListViewModel(private val repository: MainRepository) : ViewModel() {

    private val _activePostsLiveData: MutableLiveData<List<Post>> = MutableLiveData()
    val activePostsLiveData: LiveData<List<Post>> = _activePostsLiveData

    private val _userInfoLiveData: MutableLiveData<User> = MutableLiveData()
    val userInfoLiveData: LiveData<User> = _userInfoLiveData

    fun getUserPosts() {
        viewModelScope.launch {
            try {
                val response = repository.getUserPosts()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _activePostsLiveData.postValue(it.posts)
                    }
                } else {
                    // Handle unsuccessful response
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }
    fun putPostStatus(postId: String, status: String) {
        viewModelScope.launch {
            try {
                val response = repository.putPostStatus(PutPostStatusRequest(postId=postId, status=status))
                if (response.isSuccessful) {
                    // Update the active posts LiveData after successful update
                    getUserPosts()
                } else {
                    // Handle unsuccessful response
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }

}