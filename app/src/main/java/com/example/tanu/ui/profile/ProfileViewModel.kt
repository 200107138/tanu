package com.example.tanu.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.GetUserInfoResponse
import com.example.tanu.data.models.Post
import com.example.tanu.data.models.PutUserNameRequest
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

class ProfileViewModel(val repository: MainRepository) : ViewModel() {

    private val _postsLiveData: MutableLiveData<List<Post>> = MutableLiveData()
    val postsLiveData: LiveData<List<Post>> = _postsLiveData
    private val _userInfoLiveData: MutableLiveData<GetUserInfoResponse> = MutableLiveData()
    val userInfoLiveData: LiveData<GetUserInfoResponse> = _userInfoLiveData
    private val _putUserNameResultLiveData: MutableLiveData<String> = MutableLiveData()
    val putUserNameResultLiveData: LiveData<String> = _putUserNameResultLiveData

    fun putUserName(newName: String) {
        viewModelScope.launch {
            try {
                val response = repository.putUserName(PutUserNameRequest(newName))
                if (response.isSuccessful) {
                    _putUserNameResultLiveData.postValue("success")
                } else {
                    _putUserNameResultLiveData.postValue("error")
                }
            } catch (e: Exception) {
                _putUserNameResultLiveData.postValue("error")
            }
        }
    }
    fun getPostsByUserId(userId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getPostsByUserId(userId)
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
    fun getUserInfo(userId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUserInfo(userId)
                if (response?.status == "success") {
                    _userInfoLiveData.postValue(response!!)
                } else {
                    // Handle error or empty response
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }
}