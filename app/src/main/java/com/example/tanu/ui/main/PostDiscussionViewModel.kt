package com.example.tanu.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.Discussion
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

// PostDiscussionsViewModel.kt
class PostDiscussionViewModel(private val repository: MainRepository) : ViewModel() {

    private val _discussionsLiveData: MutableLiveData<List<Discussion>> = MutableLiveData()
    val discussionsLiveData: LiveData<List<Discussion>> = _discussionsLiveData

    fun getDiscussionsByPostId(postId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDiscussionsByPostId(postId)
                if (response?.status == "success") {
                    _discussionsLiveData.postValue(response.discussions)
                } else {
                    // Handle error or empty response
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }
}
