package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class DiscussionComment(
    @SerializedName("_id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("date") val date: Date,
    @SerializedName("user_avatar") val userAvatar: String,
    @SerializedName("user_email") val userEmail: String
)