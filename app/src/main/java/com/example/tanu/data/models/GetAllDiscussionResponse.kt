package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetAllDiscussionResponse(
    @SerializedName("status") val status: String,
    @SerializedName("discussions") val discussions: List<Discussion>
)