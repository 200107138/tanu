package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetPostsResponse(
    @SerializedName("posts") val posts: List<Post>,
    @SerializedName("status") val status: String,
)