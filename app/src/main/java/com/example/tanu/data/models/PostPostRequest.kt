package com.example.tanu.data.models

data class PostPostRequest(
    val description: String,
    val title: String,
    val telDonation: Long,
    val cardDonation: Long,
    val categoryId: String,
)
