package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("posts_rated") val postsRated: Int,
    @SerializedName("avatar_url") val avatarUrl: String?
)
