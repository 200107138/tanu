package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetMessagesResponse(
    @SerializedName("message") val message: String,
    @SerializedName("messages") val messages: List<Message>,
    @SerializedName("status") val status: String,
)