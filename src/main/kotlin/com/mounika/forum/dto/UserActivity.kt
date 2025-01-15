package com.mounika.forum.dto

data class UserActivity(
    val userId: String,
    val threadsCreated: List<String> = emptyList(),
    val answersPosted: List<String> = emptyList(),
    val tagsFollowed: List<String> = emptyList(),
    val categoriesFollowed: List<String> = emptyList()
)
