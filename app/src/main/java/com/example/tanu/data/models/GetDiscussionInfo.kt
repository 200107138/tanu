package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetDiscussionInfo(
    @SerializedName("discussion") val discussion: Discussion,
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String
)