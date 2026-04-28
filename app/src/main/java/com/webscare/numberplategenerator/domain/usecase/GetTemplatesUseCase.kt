package com.webscare.numberplategenerator.domain.usecase

import com.webscare.numberplategenerator.domain.model.PlateTemplate
import com.webscare.numberplategenerator.domain.repo.TemplateRepository

class GetTemplatesUseCase(private val repository: TemplateRepository) {
    suspend operator fun invoke(): List<PlateTemplate> {
        return repository.getTemplates()
    }
}