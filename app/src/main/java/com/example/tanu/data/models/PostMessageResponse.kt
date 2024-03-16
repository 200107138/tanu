package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class PostMessageResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String)