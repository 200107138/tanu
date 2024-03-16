package com.example.tanu.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.data.repository.MainRepository

class ChatRoomsViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatRoomsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatRoomsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}