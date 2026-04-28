package com.webscare.numberplategenerator.data.repository

import com.webscare.numberplategenerator.R
import com.webscare.numberplategenerator.domain.model.RecentPlate
import com.webscare.numberplategenerator.domain.repo.RecentRepository


class RecentRepositoryImpl : RecentRepository {

    override suspend fun getRecentPlates(): List<RecentPlate> {

        return listOf(
            RecentPlate(
                id = 1,
                image = R.drawable.number_plate_placeholder,
                plateNumber = "LE-23-4567",
                time = "2 mins ago"
            ),
            RecentPlate(
                id = 2,
                image = R.drawable.number_plate_placeholder,
                plateNumber = "ISB-12-9988",
                time = "1 hour ago"
            ),
            RecentPlate(
                id = 3,
                image = R.drawable.number_plate_placeholder,
                plateNumber = "LHR-77-1122",
                time = "3 hours ago"
            )
        )
    }
}