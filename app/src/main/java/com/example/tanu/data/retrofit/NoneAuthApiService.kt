package com.example.tanu.data.retrofit

import com.example.tanu.data.models.RefreshTokenRequest
import com.example.tanu.data.models.TokenResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface NoneAuthApiService {
    @POST("/refreshAccessToken")
    suspend fun refreshAccessToken(@Body refreshToken: RefreshTokenRequest): TokenResponse
}