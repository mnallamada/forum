package com.mounika.forum.controller

import com.mounika.forum.dto.UserActivity
import com.mounika.forum.repository.UserActivityRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users/activity")
class UserActivityController(private val userActivityRepository: UserActivityRepository) {

    @GetMapping("/{userId}")
    fun getUserActivity(@PathVariable userId: String): ResponseEntity<UserActivity> {
        val userActivity = userActivityRepository.getUserActivity(userId) ?: return ResponseEntity.notFound().build()

        val dto = UserActivity(
            userId = userId,
            threadsCreated = userActivity["threads_created"]?.l()?.map { it.s()!! } ?: emptyList(),
            answersPosted = userActivity["answers_posted"]?.l()?.map { it.s()!! } ?: emptyList(),
            tagsFollowed = userActivity["tags_followed"]?.ss() ?: emptyList(),
            categoriesFollowed = userActivity["categories_followed"]?.ss() ?: emptyList()
        )

        return ResponseEntity.ok(dto)
    }

    @PostMapping("/{userId}/threads")
    fun logThreadCreation(@PathVariable userId: String, @RequestParam threadId: String): ResponseEntity<Void> {
        userActivityRepository.logThreadCreation(userId, threadId)
        return ResponseEntity.ok().build()
    }

    @PostMapping("/{userId}/answers")
    fun logAnswerPost(@PathVariable userId: String, @RequestParam answerId: String): ResponseEntity<Void> {
        userActivityRepository.logAnswerPost(userId, answerId)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/{userId}/tags")
    fun updateTagsFollowed(@PathVariable userId: String, @RequestBody tags: List<String>): ResponseEntity<Void> {
        userActivityRepository.updateTagsFollowed(userId, tags)
        return ResponseEntity.ok().build()
    }

    @PutMapping("/{userId}/categories")
    fun updateCategoriesFollowed(@PathVariable userId: String, @RequestBody categories: List<String>): ResponseEntity<Void> {
        userActivityRepository.updateCategoriesFollowed(userId, categories)
        return ResponseEntity.ok().build()
    }
}
