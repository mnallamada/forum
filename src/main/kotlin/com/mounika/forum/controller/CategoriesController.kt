package com.mounika.forum.controller

import com.mounika.forum.dto.Category
import com.mounika.forum.repository.ThreadRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/categories")
@Tag(name = "Categories", description = "Get categories")
class CategoriesController(private val threadRepository: ThreadRepository) {

    @GetMapping
    @Operation(summary = "Get all categories", description = "Returns a list of all categories")
    fun getCategories(): List<Category> {
        val categoryCount = threadRepository.getCategoryCount()
        return listOf(
            Category(
                id = "breakfast",
                name = "Tiffins/Breakfast",
                icon = "sunrise",
                threadCount = categoryCount["breakfast"] ?: 0
            ),
            Category(
                id = "biryanis",
                name = "Biryanis",
                icon = "sun",
                threadCount = categoryCount["biryanis"] ?: 0
            ),
            Category(
                id = "snacks",
                name = "Evening snacks",
                icon = "sunset",
                threadCount = categoryCount["snacks"] ?: 0
            ),
            Category(
                id = "general",
                name = "General",
                icon = "person-raised-hand",
                threadCount = categoryCount["general"] ?: 0
            )
        )
    }
}