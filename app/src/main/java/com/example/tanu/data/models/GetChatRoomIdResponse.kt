package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetChatRoomIdResponse(
    @SerializedName("chatRoomId") val chatRoomId: String,
    @SerializedName("status") val status: String,
)