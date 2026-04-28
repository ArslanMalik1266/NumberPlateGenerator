package com.webscare.numberplategenerator.domain.repo

import com.webscare.numberplategenerator.domain.model.PlateTemplate

interface TemplateRepository {
    suspend fun getTemplates(): List<PlateTemplate>
}