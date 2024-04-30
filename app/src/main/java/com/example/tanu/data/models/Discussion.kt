package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class Discussion(
    @SerializedName("_id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("description") val description: String,
    @SerializedName("post") val post: Post,
    @SerializedName("user") val user: User
)