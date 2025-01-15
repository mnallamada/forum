package com.mounika.forum.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.s3.S3Client

@Configuration
class AwsConfig {

    @Bean
    fun dynamoDbClient(): DynamoDbClient {
        return DynamoDbClient.builder().build()
    }

    @Bean
    fun s3Client(): S3Client {
        return S3Client.builder().build()
    }
}