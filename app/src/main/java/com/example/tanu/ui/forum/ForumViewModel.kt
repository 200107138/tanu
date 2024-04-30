package com.example.tanu.ui.forum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.Discussion
import com.example.tanu.data.models.DiscussionCategory
import com.example.tanu.data.models.PostCategory
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

class ForumViewModel(private val repository: MainRepository) : ViewModel() {
    private val _discussionCategoriesLiveData: MutableLiveData<List<DiscussionCategory>> = MutableLiveData()
    val discussionCategoriesLiveData: LiveData<List<DiscussionCategory>> = _discussionCategoriesLiveData

    private val _discussionsLiveData: MutableLiveData<List<Discussion>> = MutableLiveData()
    val discussionsLiveData: LiveData<List<Discussion>> = _discussionsLiveData
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
    fun getAllDiscussion() {
        viewModelScope.launch {
            try {
                val response = repository.getAllDiscussion()
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
