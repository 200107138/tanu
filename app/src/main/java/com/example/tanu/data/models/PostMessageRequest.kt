package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class PostMessageRequest(
    val chatRoomId: String,
    val userId: String,
    val postId: String,
    val messageText: String
)
