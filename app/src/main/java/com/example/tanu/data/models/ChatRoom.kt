package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class ChatRoom(
    @SerializedName("_id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("post_id") val postId: String
)