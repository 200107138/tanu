package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetChatRoomsResponse(
    @SerializedName("chat_rooms") val chatRooms: List<ChatRoom>,
    @SerializedName("status") val status: String,
)