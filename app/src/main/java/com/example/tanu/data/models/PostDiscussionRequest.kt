package com.example.tanu.data.models

data class PostDiscussionRequest(
    val title: String,
    val text: String,
    val postId: String?,
    val categoryId: String
)
