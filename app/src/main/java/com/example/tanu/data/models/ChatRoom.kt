package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class ChatRoom(
    @SerializedName("_id") val id: String,
    @SerializedName("type") val type: String,
    @SerializedName("user_avatar") val userAvatar: String,
    @SerializedName("user_email") val userEmail: String,
    @SerializedName("post_media_urls") val postMediaUrls: List<String>,
    @SerializedName("last_message") val lastMessage: String,
    @SerializedName("post_id") val postId: String
)