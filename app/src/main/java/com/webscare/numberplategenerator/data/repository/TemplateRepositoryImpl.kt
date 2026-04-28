package com.webscare.numberplategenerator.data.repository

import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.domain.model.PlateTemplate
import com.webscare.numberplategenerator.domain.repo.TemplateRepository

class TemplateRepositoryImpl : TemplateRepository {
    override suspend fun getTemplates(): List<PlateTemplate> {
        // Dummy Data - Yahan future mein aap Retrofit ya Room use karenge
        return listOf(
            PlateTemplate("Punjab", "Private - White", R.drawable.number_plate_placeholder),
            PlateTemplate("Sindh", "Private - White", R.drawable.number_plate_placeholder),
            PlateTemplate("Islamabad", "Commercial", R.drawable.number_plate_placeholder),
            PlateTemplate("Punjab", "Private - White", R.drawable.number_plate_placeholder),
            PlateTemplate("Sindh", "Private - White", R.drawable.number_plate_placeholder),
            PlateTemplate("Islamabad", "Commercial", R.drawable.number_plate_placeholder),
            PlateTemplate("Punjab", "Private - White", R.drawable.number_plate_placeholder),
            PlateTemplate("Sindh", "Private - White", R.drawable.number_plate_placeholder),
            PlateTemplate("Islamabad", "Commercial", R.drawable.number_plate_placeholder),
            PlateTemplate("Punjab", "Private - White", R.drawable.number_plate_placeholder),
            PlateTemplate("Sindh", "Private - White", R.drawable.number_plate_placeholder),
            PlateTemplate("Islamabad", "Commercial", R.drawable.number_plate_placeholder),
        )
    }
}