package com.mounika.forum.dto

data class ThreadRequest(
    val title: String,
    val content: String,
    val tags: List<String>,
    val category: String,
    val userId: String
)
