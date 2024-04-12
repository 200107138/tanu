package com.example.tanu.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.launch


class SinglePostViewModel(private val repository: MainRepository) : ViewModel() {
    private val _postsLiveData: MutableLiveData<List<Post>> = MutableLiveData()
    val postsLiveData: LiveData<List<Post>> = _postsLiveData


}
