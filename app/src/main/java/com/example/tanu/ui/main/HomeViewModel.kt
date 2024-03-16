package com.example.tanu.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.Post
import com.example.tanu.data.repository.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class HomeViewModel(private val repository: MainRepository) : ViewModel() {

    private val _postsLiveData: MutableLiveData<ArrayList<Post>> = MutableLiveData()
    val postsLiveData: LiveData<ArrayList<Post>> = _postsLiveData

    private val _errorLiveData: MutableLiveData<String> = MutableLiveData()
    val errorLiveData: LiveData<String> = _errorLiveData

    private val _commentStatusLiveData: MutableLiveData<Boolean> = MutableLiveData()
    val commentStatusLiveData: LiveData<Boolean> = _commentStatusLiveData



}