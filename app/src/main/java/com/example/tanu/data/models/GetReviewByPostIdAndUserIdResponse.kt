package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetReviewByPostIdAndUserIdResponse(
    @SerializedName("comment") val comment: String,
    @SerializedName("rate") val rate: Int,
    @SerializedName("status") val status: String,
)