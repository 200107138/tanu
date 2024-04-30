package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName
import java.util.Date

data class DiscussionComment(
    @SerializedName("_id") val id: String,
    @SerializedName("text") val text: String,
    @SerializedName("user") val user: User,
    @SerializedName("date") val date: Date,
)