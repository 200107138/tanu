package com.example.tanu.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.SessionManager
import com.example.tanu.data.Models.AuthRequest
import kotlinx.coroutines.launch
import com.example.tanu.data.Repository.MainRepository

class AuthViewModel(private val repository: MainRepository) : ViewModel() {

    private val _loginStatus = MutableLiveData<Boolean>()
    val loginStatus: LiveData<Boolean> = _loginStatus

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val request = AuthRequest(email, password)
                repository.login(request)
                _loginStatus.postValue(true) // Login successful
            } catch (e: Exception) {
                // Handle login failure or network error
                // Maybe show an error message to the user
                _loginStatus.postValue(false)
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                val request = AuthRequest(email, password)
                repository.register(request)
            } catch (e: Exception) {
            }
        }
    }
}