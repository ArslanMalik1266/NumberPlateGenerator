package com.webscare.numberplategenerator.data.remote.dto

data class FontDto(
    val id: Int,
    val name: String,
    val image_preview: String,
    val font_file: String,
    val description: String?,
    val category: CategoryDto?
)

data class CategoryDto(
    val id: Int,
    val name: String
)