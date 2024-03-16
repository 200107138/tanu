package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("_id") val id: String,
    @SerializedName("sender_id") val senderId: String,
    @SerializedName("text") val text: String
)