package com.example.tanu.Repository


import com.example.tanu.Retrofit.ApiClient
import com.example.tanu.Retrofit.ApiService

class MainRepository(private val retrofitService: ApiService) {

    suspend fun getAllPosts() = retrofitService.getAllPosts()

}