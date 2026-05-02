package com.webscare.numberplategenerator.domain.repo

import com.webscare.numberplategenerator.domain.model.FontOption

interface FontRepository {
    suspend fun getUrduFonts(): List<FontOption>
}