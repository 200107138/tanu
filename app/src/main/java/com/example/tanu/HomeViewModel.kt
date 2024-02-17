package com.example.tanu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.Models.Post
import com.example.tanu.Repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class HomeViewModel(private val repository: MainRepository) : ViewModel() {

    private val _postsLiveData: MutableLiveData<ArrayList<Post>> = MutableLiveData()
    val postsLiveData: LiveData<ArrayList<Post>> = _postsLiveData

    private val _errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorLiveData: LiveData<String> = _errorLiveData

    fun fetchData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = repository.getAllPosts()
                if (response.isSuccessful) {
                    _postsLiveData.postValue(response.body())
                } else {
                    _errorLiveData.postValue("Failed to get posts")
                }
            } catch (e: Exception) {
                _errorLiveData.postValue("Network error")
            }
        }
    }
}