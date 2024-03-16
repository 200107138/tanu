package com.example.tanu.data.retrofit

import com.example.tanu.data.models.LoginResponse
import com.example.tanu.data.models.Post
import com.example.tanu.data.models.RegisterResponse
import com.example.tanu.data.models.AuthRequest
import com.example.tanu.data.models.CommentRequest
import com.example.tanu.data.models.CommentResponse
import com.example.tanu.data.models.ChatRoomResponse
import com.example.tanu.data.models.GetMessagesResponse
import com.example.tanu.data.models.PostMessageRequest
import com.example.tanu.data.models.PostMessageResponse
import com.example.tanu.data.models.User
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("/login")
    suspend fun login(@Body request: AuthRequest): Response<LoginResponse>

    @POST("/register")
    suspend fun register(@Body request: AuthRequest): Response<RegisterResponse>

    @GET("/getChatRooms")
    suspend fun getChatRooms(): Response<ChatRoomResponse>

    @GET("/getMessages")
    suspend fun getMessages(@Query("chatRoomId") chatRoomId: String): Response<GetMessagesResponse>

    @POST("/postMessage")
    suspend fun postMessage(@Body request: PostMessageRequest): Response<PostMessageResponse>
}