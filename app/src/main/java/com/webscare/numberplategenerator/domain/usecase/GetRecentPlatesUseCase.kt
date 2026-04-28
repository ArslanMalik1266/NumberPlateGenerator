package com.webscare.numberplategenerator.domain.usecase

import com.webscare.numberplategenerator.domain.model.RecentPlate
import com.webscare.numberplategenerator.domain.repo.RecentRepository

class GetRecentPlatesUseCase(private val repository: RecentRepository) {
    suspend operator fun invoke(): List<RecentPlate> {
        return repository.getRecentPlates()
    }
}