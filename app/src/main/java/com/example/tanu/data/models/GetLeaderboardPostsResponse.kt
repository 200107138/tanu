package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetLeaderboardPostsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("posts") val posts: List<Post>
)