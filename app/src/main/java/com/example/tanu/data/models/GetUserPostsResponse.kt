package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetUserPostsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("posts") val posts: List<Post>
)
