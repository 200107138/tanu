package com.example.tanu.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.GetUserInfoResponse
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

class PostDescriptionViewModel(private val repository: MainRepository) : ViewModel() {
    private val _postMessageLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val postMessageLiveData: LiveData<Boolean> = _postMessageLiveData
    private val _userInfoLiveData: MutableLiveData<GetUserInfoResponse> = MutableLiveData()
    val userInfoLiveData: LiveData<GetUserInfoResponse> = _userInfoLiveData

    fun getUserInfo(userId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getUserInfo(userId)
                _userInfoLiveData.value = response!!
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

}