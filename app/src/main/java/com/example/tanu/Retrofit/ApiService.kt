package com.example.tanu.Retrofit

import android.provider.SyncStateContract
import com.example.tanu.Models.Post
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("getAllPosts")
    suspend fun getAllPosts() : Response<ArrayList<Post>>
}