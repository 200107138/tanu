package com.example.tanu.data.repository

import android.util.Log
import com.example.tanu.SessionManager
import com.example.tanu.data.models.AuthRequest
import com.example.tanu.data.models.ChatRoomResponse
import com.example.tanu.data.models.LoginResponse
import com.example.tanu.data.retrofit.ApiService
import com.example.tanu.data.UserHolder
import com.example.tanu.data.models.GetMessagesResponse
import com.example.tanu.data.models.PostMessageRequest
import com.example.tanu.data.models.PostMessageResponse
import retrofit2.Callback
import retrofit2.Call
import retrofit2.Response

class MainRepository(private val apiService: ApiService, private val sessionManager: SessionManager) {
    private val TAG = "MainRepository"
    suspend fun login(request: AuthRequest): LoginResponse? {
        try {
            val response = apiService.login(request)
            return if (response.isSuccessful) {
                val accessToken = response.body()?.accessToken
                val refreshToken = response.body()?.refreshToken
                accessToken?.let { token ->
                    refreshToken?.let { refresh ->
                        sessionManager.saveTokens(token, refresh)
                    }
                }
                UserHolder.userId = response.body()?.userId
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            throw e
        }
    }





    suspend fun register(request: AuthRequest) {
            val response = apiService.register(request)
            if (response.isSuccessful) {
                Log.e(TAG, "Registration ssssss")
            } else {
                throw Exception("Registration failed") // Handle registration failure
                Log.e(TAG, "Registration failed")
            }
        }

    suspend fun getChatRooms(): Response<ChatRoomResponse> = apiService.getChatRooms()

    suspend fun getMessages(chatRoomId: String): GetMessagesResponse? {
            try {
                val response = apiService.getMessages(chatRoomId)
                return if (response.isSuccessful) {
                    response.body() // Return the response body
                } else {
                    null
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error sending message: ${e.message}")
                throw e
            }
        }



    suspend fun postMessage(chatRoomId: String, messageText: String): PostMessageResponse? {
        try {
            val response = apiService.postMessage(PostMessageRequest(chatRoomId, messageText))
            return if (response.isSuccessful) {
                response.body() // Return the response body
            } else {
                null
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error sending message: ${e.message}")
            throw e
        }
    }
}