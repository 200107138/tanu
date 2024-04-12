package com.example.tanu.ui.forum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.PostDiscussionRequest
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

class NewDiscussionViewModel(private val repository: MainRepository) : ViewModel() {

    private val _postDiscussionLiveData = MutableLiveData<Boolean>()
    val postDiscussionLiveData: LiveData<Boolean> get() = _postDiscussionLiveData

    fun postDiscussion(title: String, text: String, postId: String) {
        val request = PostDiscussionRequest(title, text, postId)
        viewModelScope.launch {
            try {
                val response = repository.postDiscussion(request)
                _postDiscussionLiveData.value = response?.status == "success"
            } catch (e: Exception) {
                _postDiscussionLiveData.value = false
            }
        }
    }
}