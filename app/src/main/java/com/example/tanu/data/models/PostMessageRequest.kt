package com.example.tanu.data.models

data class PostMessageRequest(
    val messageText: String,
    val chatRoomId: String,
    val postId: String,
    val receiverId: String
)
