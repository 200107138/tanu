package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetDiscussionsByPostIdResponse(
    @SerializedName("discussions") val discussions: List<Discussion>,
    @SerializedName("status") val status: String,
)