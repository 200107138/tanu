package com.example.tanu.data.Retrofit

import com.example.tanu.data.Models.LoginResponse
import com.example.tanu.data.Models.Post
import com.example.tanu.data.Models.RegisterResponse
import com.example.tanu.data.Models.AuthRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("getAllPosts")
    suspend fun getAllPosts(): Response<ArrayList<Post>>

    @POST("login")
    suspend fun login(@Body request: AuthRequest): Response<LoginResponse>

    @POST("register")
    suspend fun register(@Body request: AuthRequest): Response<RegisterResponse>
}