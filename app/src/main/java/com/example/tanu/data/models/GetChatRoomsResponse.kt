package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetChatRoomsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("chatRooms") val chatRooms: List<ChatRoom>
)
