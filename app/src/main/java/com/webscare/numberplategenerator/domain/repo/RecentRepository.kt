package com.webscare.numberplategenerator.domain.repo

import com.webscare.numberplategenerator.domain.model.RecentPlate

interface RecentRepository {
    suspend fun getRecentPlates(): List<RecentPlate>
}