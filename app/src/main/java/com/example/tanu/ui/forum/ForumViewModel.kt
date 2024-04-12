package com.example.tanu.ui.forum

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.Discussion
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch

class ForumViewModel(private val repository: MainRepository) : ViewModel() {

    private val _discussionsLiveData: MutableLiveData<List<Discussion>> = MutableLiveData()
    val discussionsLiveData: LiveData<List<Discussion>> = _discussionsLiveData

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
