package com.webscare.numberplategenerator.di

import com.webscare.numberplategenerator.domain.usecase.GetRecentPlatesUseCase
import com.webscare.numberplategenerator.domain.usecase.GetTemplatesUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetTemplatesUseCase(get()) }
    factory { GetRecentPlatesUseCase(get()) }
}