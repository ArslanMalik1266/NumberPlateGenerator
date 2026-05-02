package com.webscare.numberplategenerator.di

import com.webscare.numberplategenerator.domain.usecase.GetRecentPlatesUseCase
import com.webscare.numberplategenerator.domain.usecase.GetTemplatesUseCase
import com.webscare.numberplategenerator.domain.usecase.GetUrduFontsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetTemplatesUseCase(get()) }
    factory { GetRecentPlatesUseCase(get()) }
    factory { GetUrduFontsUseCase(get()) }
}
