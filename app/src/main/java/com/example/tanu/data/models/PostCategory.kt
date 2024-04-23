package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PostCategory(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("image") val image: String
) : Serializable