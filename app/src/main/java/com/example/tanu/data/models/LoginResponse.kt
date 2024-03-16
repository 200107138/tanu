package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("user_id") val userId: String,
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("message") val message: String,
    @SerializedName("status") val status: String
)
