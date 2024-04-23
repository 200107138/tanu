package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class Discussion(
    @SerializedName("_id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("text") val text: String,
    @SerializedName("post_id") val postId: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("user_avatar_url") val userAvatarUrl: String,
    @SerializedName("user_email") val userEmail: String,
    @SerializedName("category_id") val categoryId: String,
)