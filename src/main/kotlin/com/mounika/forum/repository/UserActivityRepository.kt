package com.mounika.forum.repository

import org.springframework.stereotype.Repository
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeValue

@Repository
class UserActivityRepository(private val dynamoDb: DynamoDbClient) {
    private val tableName = "UserActivityLogs"

    fun getUserActivity(userId: String): Map<String, AttributeValue>? {
        val response = dynamoDb.getItem {
            it.tableName(tableName).key(mapOf("user_id" to AttributeValue.builder().s(userId).build()))
        }
        return response.item()
    }

    fun logThreadCreation(userId: String, threadId: String) {
        dynamoDb.updateItem {
            it.tableName(tableName)
            it.key(mapOf("user_id" to AttributeValue.builder().s(userId).build()))
            it.updateExpression("SET threads_created = list_append(if_not_exists(threads_created, :emptyList), :newThread)")
            it.expressionAttributeValues(
                mapOf(
                    ":emptyList" to AttributeValue.builder().l(emptyList<AttributeValue>()).build(),
                    ":newThread" to AttributeValue.builder().l(AttributeValue.builder().s(threadId).build()).build()
                )
            )
        }
    }

    fun logAnswerPost(userId: String, answerId: String) {
        dynamoDb.updateItem {
            it.tableName(tableName)
            it.key(mapOf("user_id" to AttributeValue.builder().s(userId).build()))
            it.updateExpression("SET answers_posted = list_append(if_not_exists(answers_posted, :emptyList), :newAnswer)")
            it.expressionAttributeValues(
                mapOf(
                    ":emptyList" to AttributeValue.builder().l(emptyList<AttributeValue>()).build(),
                    ":newAnswer" to AttributeValue.builder().l(AttributeValue.builder().s(answerId).build()).build()
                )
            )
        }
    }

    fun updateTagsFollowed(userId: String, tags: List<String>) {
        dynamoDb.updateItem {
            it.tableName(tableName)
            it.key(mapOf("user_id" to AttributeValue.builder().s(userId).build()))
            it.updateExpression("SET tags_followed = :tags")
            it.expressionAttributeValues(
                mapOf(":tags" to AttributeValue.builder().ss(tags).build())
            )
        }
    }

    fun updateCategoriesFollowed(userId: String, categories: List<String>) {
        dynamoDb.updateItem {
            it.tableName(tableName)
            it.key(mapOf("user_id" to AttributeValue.builder().s(userId).build()))
            it.updateExpression("SET categories_followed = :categories")
            it.expressionAttributeValues(
                mapOf(":categories" to AttributeValue.builder().ss(categories).build())
            )
        }
    }
}
