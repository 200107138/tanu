package com.example.tanu.ui.forum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.DiscussionCategory
import com.example.tanu.data.models.PostCategory
import com.example.tanu.data.models.PostDiscussionRequest
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

class NewDiscussionViewModel(private val repository: MainRepository) : ViewModel() {
    private val _discussionCategoriesLiveData: MutableLiveData<List<DiscussionCategory>> = MutableLiveData()
    val discussionCategoriesLiveData: LiveData<List<DiscussionCategory>> = _discussionCategoriesLiveData

    private val _postDiscussionLiveData = MutableLiveData<Boolean>()
    val postDiscussionLiveData: LiveData<Boolean> get() = _postDiscussionLiveData

    fun postDiscussion(title: String, description: String, categoryId: String) {
        val request = PostDiscussionRequest(title = title, description = description, postId = null, categoryId = categoryId)
        viewModelScope.launch {
            try {
                val response = repository.postDiscussion(request)
                _postDiscussionLiveData.value = response?.status == "success"
            } catch (e: Exception) {
                _postDiscussionLiveData.value = false
            }
        }
    }
    fun getDiscussionCategories() {
        viewModelScope.launch {
            try {
                val response = repository.getDiscussionCategories()
                if (response.isSuccessful) {
                    response.body()?.let {
                        _discussionCategoriesLiveData.postValue(it.categories)
                    }
                } else {

                }
            } catch (e: Exception) {

            }
        }
    }
}