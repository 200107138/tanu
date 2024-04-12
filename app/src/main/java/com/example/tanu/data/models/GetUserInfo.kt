package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetUserInfoResponse(
    @SerializedName("user") val user: User,
    @SerializedName("status") val status: String,
)