package com.webscare.numberplategenerator.di

import com.webscare.numberplategenerator.data.repository.RecentRepositoryImpl
import com.webscare.numberplategenerator.data.repository.TemplateRepositoryImpl
import com.webscare.numberplategenerator.domain.repo.RecentRepository
import com.webscare.numberplategenerator.domain.repo.TemplateRepository
import org.koin.dsl.module

val dataModule = module {
    single<TemplateRepository> { TemplateRepositoryImpl() }
    single<RecentRepository> { RecentRepositoryImpl() }
}