package com.example.tanu.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tanu.data.models.AuthRequest
import kotlinx.coroutines.launch
import com.example.tanu.data.repository.MainRepository

class AuthViewModel(private val repository: MainRepository) : ViewModel() {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean>
        get() = _loginSuccess

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = repository.login(AuthRequest(email, password))
                _loginSuccess.value = response?.status == "success"
            } catch (e: Exception) {
                _loginSuccess.value = false
            }
        }
    }

    fun register(email: String, password: String) {
        viewModelScope.launch {
            try {
                val request = AuthRequest(email, password)
                repository.register(request)
            } catch (e: Exception) {
                // Handle registration failure
            }
        }
    }
}
