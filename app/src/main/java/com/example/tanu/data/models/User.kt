package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("avatar_url") val avatarUrl: String
)
