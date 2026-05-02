package com.webscare.numberplategenerator.data.mapper

import com.webscare.numberplategenerator.data.remote.dto.FontDto
import com.webscare.numberplategenerator.domain.model.FontOption

fun FontDto.toDomain(): FontOption {
    return FontOption(
        id = this.id.toString(),
        name = this.name,
        previewImage = this.image_preview,
        fontUrl = this.font_file,
        categoryName = this.category?.name ?: "General"
    )
}