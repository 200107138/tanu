package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Post(
    @SerializedName("_id") val id: String,
    @SerializedName("description") val description: String,
    @SerializedName("type") val type: String,
    @SerializedName("media_urls") val mediaUrls: ArrayList<String>,
    @SerializedName("rating") val rating: Int,
    @SerializedName("user_id") val userId: String
) : Serializable