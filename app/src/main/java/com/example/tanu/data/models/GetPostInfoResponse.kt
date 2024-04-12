package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetPostInfoResponse(
    @SerializedName("post") val post: Post,
    @SerializedName("status") val status: String,
)