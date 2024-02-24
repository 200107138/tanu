package com.example.tanu.data.Repository


import android.util.Log
import com.example.tanu.SessionManager
import com.example.tanu.data.Models.LoginResponse
import com.example.tanu.data.Models.Post
import com.example.tanu.data.Models.RegisterResponse
import com.example.tanu.data.Models.AuthRequest
import com.example.tanu.data.Models.CommentRequest
import com.example.tanu.data.Retrofit.ApiService

import retrofit2.Response

class MainRepository(private val apiService: ApiService, private val sessionManager: SessionManager) {
    private val TAG = "MainRepository"
    suspend fun getAllPosts(): Response<ArrayList<Post>> = apiService.getAllPosts()

    suspend fun login(request: AuthRequest) {
        val response = apiService.login(request)
        if (response.isSuccessful) {
            Log.d(TAG, "loginreposit")
            // Save login credentials if login is successful
            sessionManager.saveCredentials(request.email, request.password)
        } else {
            throw Exception("Login failed") // Handle login failure
        }
    }

    suspend fun register(request: AuthRequest) {
        val response = apiService.register(request)
        if (response.isSuccessful) {
            // Save registration credentials if registration is successful
            sessionManager.saveCredentials(request.email, request.password)
        } else {
            throw Exception("Registration failed") // Handle registration failure
        }
    }

    suspend fun postComment(postId: String, text: String) {
        try {
            val response = apiService.postComment(CommentRequest(postId, text))
            if (!response.isSuccessful) {
                throw Exception("Failed to post comment")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error posting comment: ${e.message}")
            throw e
        }
    }
}
