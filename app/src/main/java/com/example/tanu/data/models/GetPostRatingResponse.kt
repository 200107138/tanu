package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetPostRatingResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("rate") val rate: Int
)
