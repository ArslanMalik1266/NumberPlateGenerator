package com.webscare.numberplategenerator.data.remote

import com.webscare.numberplategenerator.data.remote.dto.FontDto
import retrofit2.http.GET

interface FontApiService {
    @GET("api/urdu-fonts")
    suspend fun getFonts(): List<FontDto>
}