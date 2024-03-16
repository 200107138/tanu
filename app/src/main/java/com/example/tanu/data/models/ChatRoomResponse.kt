package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class ChatRoomResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("chat_rooms") val chatRooms: List<ChatRoom>
)
