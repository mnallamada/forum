package com.mounika.forum.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ThreadDto @JsonCreator constructor(
    @JsonProperty("thread_id") val threadId: String,
    @JsonProperty("user_id") val userId: String,
    @JsonProperty("title") val title: String,
    @JsonProperty("description") val description: String,
    @JsonProperty("category") val category: String,
    @JsonProperty("tags") val tags: List<String>? = emptyList(),
    @JsonProperty("view_count") val viewCount: Int,
    @JsonProperty("likes_count") val likesCount: Int,
    @JsonProperty("comments") val comments: List<com.mounika.forum.dto.CommentDto>? = emptyList(),
    @JsonProperty("comments_count") val commentsCount: Int,
    @JsonProperty("created_at") val createdAt: String
)

data class CommentDto @JsonCreator constructor(
    @JsonProperty("comment_id") val commentId: String? = null,
    @JsonProperty("user_id") val userId: String? = null,
    @JsonProperty("content") val content: String? = null,
    @JsonProperty("content_source") val contentSource: String? = null,
    @JsonProperty("created_at") val createdAt: String? = null
)