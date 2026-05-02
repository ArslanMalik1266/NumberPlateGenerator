package com.webscare.numberplategenerator.data.repository

import com.webscare.numberplategenerator.data.mapper.toDomain
import com.webscare.numberplategenerator.data.remote.FontApiService
import com.webscare.numberplategenerator.domain.model.FontOption
import com.webscare.numberplategenerator.domain.repo.FontRepository
import kotlin.collections.map

class FontRepositoryImpl(
    private val apiService: FontApiService
) : FontRepository {

    override suspend fun getUrduFonts(): List<FontOption> {
        return try {
            val response = apiService.getFonts() // Maan lete hain ye List<FontDto> hai

            // 1. RAW API data check karein
            println("DEBUG_REPO: Raw API response received. Size: ${response.size}")

            if (response.isEmpty()) {
                println("DEBUG_REPO: Server returned an empty list.")
            } else {
                response.forEach { dto ->
                    // Check karein ke DTO mein data sahi aa raha hai
                    println("DEBUG_REPO: DTO Item - ID: ${dto.id}, Name: ${dto.name}, Preview: ${dto.image_preview}")
                }
            }

            // Mapping process
            val domainList = response.map { it.toDomain() }

            // 2. Mapping ke baad check karein
            println("DEBUG_REPO: Mapping complete. Domain list size: ${domainList.size}")

            domainList
        } catch (e: Exception) {
            println("DEBUG_REPO: Exception in Repository: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }
}