package com.mounika.forum.controller

import com.mounika.forum.dto.CommentDto
import com.mounika.forum.dto.ThreadDto
import com.mounika.forum.dto.ThreadRequest
import com.mounika.forum.repository.ThreadRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

@RestController
@RequestMapping("/threads")
class ThreadController(
    private val threadRepository: ThreadRepository
) {

    @GetMapping
    fun getAllThreads(): ResponseEntity<List<com.mounika.forum.dto.ThreadDto>> {
        val threads = threadRepository.getAllThreads()
        return ResponseEntity.ok(threads)
    }

    @GetMapping("/category-count")
    fun getCategoryCount(): ResponseEntity<Map<String, Long>> {
        val categoryCount = threadRepository.getCategoryCount()
        return ResponseEntity.ok(categoryCount)
    }

    @GetMapping("/category/{category}")
    fun getThreadsByCategory(@PathVariable category: String): ResponseEntity<List<com.mounika.forum.dto.ThreadDto>> {
        val threads = threadRepository.getThreadsByCategory(category)
        return ResponseEntity.ok(threads)
    }

    @PostMapping("/create")
    fun createThread(@RequestBody request: ThreadRequest): ResponseEntity<com.mounika.forum.dto.ThreadDto> {
        val currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME)
        val threadId = UUID.randomUUID().toString()
        val thread = com.mounika.forum.dto.ThreadDto(
            threadId = threadId,
            userId = request.userId,
            title = request.title,
            description = request.content,
            category = request.category,
            tags = request.tags,
            viewCount = 0,
            likesCount = 0,
            comments = emptyList(),
            commentsCount = 0,
            createdAt = currentDateTime
        )
        val createdThread = threadRepository.createThread(thread)
        return ResponseEntity.ok(createdThread)
    }

    @GetMapping("/{id}")
    fun getThread(@PathVariable id: String): ResponseEntity<com.mounika.forum.dto.ThreadDto> {
        val thread = threadRepository.getThreadById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(thread)
    }


    @PostMapping("/{id}/add-comment")
    fun addCommentToThread(@PathVariable id: String, @RequestBody comment: com.mounika.forum.dto.CommentDto): ResponseEntity<Void> {
        threadRepository.getThreadById(id) ?: return ResponseEntity.notFound().build()
        threadRepository.addCommentToThread(id, comment)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{id}/increment-view")
    fun incrementView(@PathVariable id: String): ResponseEntity<Void> {
        threadRepository.incrementViewCount(id)
        return ResponseEntity.ok().build()
    }
}