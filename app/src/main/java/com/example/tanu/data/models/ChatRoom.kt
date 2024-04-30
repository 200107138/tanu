package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class ChatRoom(
    @SerializedName("_id") val id: String,
    @SerializedName("sender") val sender: User,
    @SerializedName("receiver") val receiver: User,
    @SerializedName("last_message") val lastMessage: String,
    @SerializedName("post") val post: Post
)