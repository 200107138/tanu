package com.example.tanu.data.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Post(
    @SerializedName("_id") val id: String,
    @SerializedName("description") val description: String,
    @SerializedName("media_urls") val mediaUrls: ArrayList<String>,
    @SerializedName("rating") val rating: Double,
    @SerializedName("user") val user: User,
    @SerializedName("title") val title: String,
    @SerializedName("tel_donation") val telDonation: String,
    @SerializedName("status") val status: String,
    @SerializedName("card_donation") val cardDonation: String,
    @SerializedName("category_id") val categoryId: String,
    @SerializedName("rated") val rated: Boolean,
    @SerializedName("myRate") val myRate: Int
) : Serializable