package com.mounika.forum.dto

data class Category(
    val id: String,
    val name: String,
    val icon: String,
    val threadCount: Long
)