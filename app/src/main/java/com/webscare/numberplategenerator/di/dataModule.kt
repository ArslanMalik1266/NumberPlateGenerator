package com.webscare.numberplategenerator.di

import com.webscare.numberplategenerator.data.remote.FontApiService
import com.webscare.numberplategenerator.data.repository.FontRepositoryImpl
import com.webscare.numberplategenerator.data.repository.RecentRepositoryImpl
import com.webscare.numberplategenerator.data.repository.TemplateRepositoryImpl
import com.webscare.numberplategenerator.domain.repo.FontRepository
import com.webscare.numberplategenerator.domain.repo.RecentRepository
import com.webscare.numberplategenerator.domain.repo.TemplateRepository
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single {
        Retrofit.Builder()
            .baseUrl("https://fonts.shabbirhussain.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // API Service
    single { get<Retrofit>().create(FontApiService::class.java) }

    single<TemplateRepository> { TemplateRepositoryImpl() }
    single<RecentRepository> { RecentRepositoryImpl() }
    single<FontRepository> { FontRepositoryImpl(get()) }
}