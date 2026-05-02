package com.webscare.numberplategenerator.domain.usecase

import com.webscare.numberplategenerator.domain.model.FontOption
import com.webscare.numberplategenerator.domain.repo.FontRepository

class GetUrduFontsUseCase(private val repository: FontRepository) {
    suspend operator fun invoke(): List<FontOption> {
        return repository.getUrduFonts()
    }
}