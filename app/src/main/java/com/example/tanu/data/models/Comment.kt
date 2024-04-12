package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class Comment(
    @SerializedName("_id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("post_id") val postId: String,
    @SerializedName("user_id") val userId: String
)