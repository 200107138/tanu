package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("_id") val postId: String,
    @SerializedName("user_avatar") val userAvatar: String,
    @SerializedName("description") val description: String,
    @SerializedName("media_url") val mediaUrl: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("tags") val tags: List<String>
)