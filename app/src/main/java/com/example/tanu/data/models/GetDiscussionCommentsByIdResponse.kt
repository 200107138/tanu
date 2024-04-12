package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class GetDiscussionCommentsByIdResponse(
    @SerializedName("comments") val comments: List<DiscussionComment>,
    @SerializedName("status") val status: String,
)