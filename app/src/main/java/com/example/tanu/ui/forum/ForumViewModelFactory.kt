package com.example.tanu.ui.forum
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.data.repository.MainRepository

class ForumViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForumViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ForumViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
