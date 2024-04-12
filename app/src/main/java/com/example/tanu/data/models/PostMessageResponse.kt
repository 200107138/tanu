package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class PostMessageResponse(
    @SerializedName("status") val status: String,
    @SerializedName("chat_room_id") val chatRoomId: String,
    @SerializedName("message") val message: String)