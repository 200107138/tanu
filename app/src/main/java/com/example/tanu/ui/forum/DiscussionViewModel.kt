// DiscussionViewModel.kt
package com.example.tanu.ui.forum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.Discussion
import com.example.tanu.data.models.DiscussionComment
import com.example.tanu.data.models.PostDiscussionCommentRequest
import com.example.tanu.data.models.PostDiscussionRequest
import com.example.tanu.data.models.User
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

class DiscussionViewModel(private val repository: MainRepository) : ViewModel() {

    private val _discussionCommentsLiveData: MutableLiveData<List<DiscussionComment>> = MutableLiveData()
    val discussionCommentsLiveData: LiveData<List<DiscussionComment>> = _discussionCommentsLiveData

    private val _discussionInfoLiveData: MutableLiveData<Discussion> = MutableLiveData()
    val discussionInfoLiveData: LiveData<Discussion> = _discussionInfoLiveData

    private val _postCommentStatus: MutableLiveData<Boolean> = MutableLiveData()
    val postCommentStatus: LiveData<Boolean> = _postCommentStatus

    // Function to post a discussion comment
    fun postDiscussionComment(discussionId: String, text: String) {
        val request = PostDiscussionCommentRequest(text, discussionId)
        viewModelScope.launch {
            try {
                val response = repository.postDiscussionComment(request)
                if (response?.status == "success") {
                    _postCommentStatus.postValue(true)
                } else {
                    _postCommentStatus.postValue(false)
                }
            } catch (e: Exception) {
                _postCommentStatus.postValue(false)
            }
        }
    }
    fun getDiscussionCommentsById(discussionId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDiscussionCommentsById(discussionId)
                if (response?.status == "success") {
                    _discussionCommentsLiveData.postValue(response.comments)
                } else {
                    // Handle error or empty response
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }

    fun getDiscussionInfo(discussionId: String) {
        viewModelScope.launch {
            try {
                val response = repository.getDiscussionInfo(discussionId)
                if (response?.status == "success") {
                    _discussionInfoLiveData.postValue(response.discussion)
                } else {
                    // Handle error or empty response
                }
            } catch (e: Exception) {
                // Handle network or other errors
            }
        }
    }

}
