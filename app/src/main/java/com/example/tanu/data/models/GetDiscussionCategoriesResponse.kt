package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetDiscussionCategoriesResponse(
    @SerializedName("status") val status: String,
    @SerializedName("message") val message: String,
    @SerializedName("categories") val categories: List<DiscussionCategory>
)
