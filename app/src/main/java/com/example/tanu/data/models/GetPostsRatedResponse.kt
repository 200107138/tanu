package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetPostsRatedResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("postsRated") val postsRated: Int
)
