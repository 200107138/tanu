package com.example.tanu.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.data.repository.MainRepository
import com.example.tanu.ui.forum.NewDiscussionViewModel

class NewPostDiscussionViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewPostDiscussionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewPostDiscussionViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}