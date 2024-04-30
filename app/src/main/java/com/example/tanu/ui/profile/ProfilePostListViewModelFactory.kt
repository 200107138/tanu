package com.example.tanu.ui.profile
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tanu.data.repository.MainRepository

class ProfilePostListViewModelFactory(private val repository: MainRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfilePostListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfilePostListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}