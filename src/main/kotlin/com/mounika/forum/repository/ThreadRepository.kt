package com.mounika.forum.repository
import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import java.time.Instant

@Repository
class ThreadRepository(private val dynamoDb: DynamoDbClient) {

    private val tableName = "forum"

    fun createThread(thread: com.mounika.forum.dto.ThreadDto): com.mounika.forum.dto.ThreadDto {
        val item = mapThreadDtoToItem(thread.copy(createdAt = Instant.now().toString()))
        dynamoDb.putItem { it.tableName(tableName).item(item) }
        return thread
    }

    fun incrementViewCount(threadId: String) {
        val thread = getThreadById(threadId) ?: return
        val updatedThread = thread.copy(viewCount = thread.viewCount + 1)
        updateThread(updatedThread)
    }

    fun updateThread(thread: com.mounika.forum.dto.ThreadDto) {
        val item = mapThreadDtoToItem(thread)
        dynamoDb.putItem { it.tableName(tableName).item(item) }
    }

    fun getThreadById(threadId: String): com.mounika.forum.dto.ThreadDto? {
        val response = dynamoDb.getItem {
            it.tableName(tableName).key(mapOf("thread_id" to AttributeValue.builder().s(threadId).build()))
        }
        val item = response.item() ?: return null
        return mapItemToThreadDto(item)
    }

    fun getAllThreads(): List<com.mounika.forum.dto.ThreadDto> {
        val response = dynamoDb.scan { it.tableName(tableName) }
        return response.items().map { mapItemToThreadDto(it) }
    }

    fun getCategoryCount(): Map<String, Long> {
        val response = dynamoDb.scan { it.tableName(tableName) }
        return response.items().groupBy { it["category"]?.s() ?: "General" }.mapValues { it.value.size.toLong() }
    }

    fun getThreadsByCategory(category: String): List<com.mounika.forum.dto.ThreadDto> {
        val response = dynamoDb.query {
            it.tableName(tableName)
            it.indexName("category-index")
            it.keyConditionExpression("category = :category")
            it.expressionAttributeValues(mapOf(":category" to AttributeValue.builder().s(category).build()))
        }
        return response.items().map { mapItemToThreadDto(it) }
    }

    fun addCommentToThread(threadId: String, comment: com.mounika.forum.dto.CommentDto) {
        val thread = getThreadById(threadId) ?: return
        val updatedComments = thread.comments?.plus(comment)
        val updatedThread = thread.copy(comments = updatedComments, commentsCount = updatedComments?.size ?: 0)
        updateThread(updatedThread)
    }

    private fun mapThreadDtoToItem(thread: com.mounika.forum.dto.ThreadDto): Map<String, AttributeValue> {
        val item = mutableMapOf(
            "thread_id" to AttributeValue.builder().s(thread.threadId).build(),
            "user_id" to AttributeValue.builder().s(thread.userId).build(),
            "title" to AttributeValue.builder().s(thread.title).build(),
            "description" to AttributeValue.builder().s(thread.description).build(),
            "category" to AttributeValue.builder().s(thread.category).build(),
            "view_count" to AttributeValue.builder().n(thread.viewCount.toString()).build(),
            "likes_count" to AttributeValue.builder().n(thread.likesCount.toString()).build(),
            "comments" to AttributeValue.builder().l(thread.comments?.map { comment ->
                AttributeValue.builder().m(mapOf(
                    "comment_id" to AttributeValue.builder().s(comment.commentId).build(),
                    "user_id" to AttributeValue.builder().s(comment.userId).build(),
                    "content" to AttributeValue.builder().s(comment.content).build(),
                    "content_source" to AttributeValue.builder().s(comment.contentSource).build(),
                    "created_at" to AttributeValue.builder().s(comment.createdAt).build()
                )).build()
            }).build(),
            "comments_count" to AttributeValue.builder().n(thread.commentsCount.toString()).build(),
            "created_at" to AttributeValue.builder().s(thread.createdAt).build()
        )
        if (!thread.tags.isNullOrEmpty()) {
            item["tags"] = AttributeValue.builder().ss(thread.tags).build()
        }
        return item
    }

    private fun mapItemToThreadDto(item: Map<String, AttributeValue>): com.mounika.forum.dto.ThreadDto {
        return com.mounika.forum.dto.ThreadDto(
            threadId = item["thread_id"]?.s() ?: "",
            userId = item["user_id"]?.s() ?: "",
            title = item["title"]?.s() ?: "",
            description = item["description"]?.s() ?: "",
            category = item["category"]?.s() ?: "",
            tags = item["tags"]?.ss() ?: emptyList(),
            viewCount = item["view_count"]?.n()?.toInt() ?: 0,
            likesCount = item["likes_count"]?.n()?.toInt() ?: 0,
            comments = item["comments"]?.l()?.map { comment ->
                com.mounika.forum.dto.CommentDto(
                    commentId = comment.m()["comment_id"]?.s() ?: "",
                    userId = comment.m()["user_id"]?.s() ?: "",
                    content = comment.m()["content"]?.s() ?: "",
                    contentSource = comment.m()["content_source"]?.s() ?: "",
                    createdAt = comment.m()["created_at"]?.s() ?: ""
                )
            } ?: emptyList(),
            commentsCount = item["comments_count"]?.n()?.toInt() ?: 0,
            createdAt = item["created_at"]?.s() ?: ""
        )
    }
}