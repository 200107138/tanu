package com.example.tanu
import com.example.tanu.Models.Post
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {
    @GET("/")
    fun getPosts(): Call<List<Post>>
}
