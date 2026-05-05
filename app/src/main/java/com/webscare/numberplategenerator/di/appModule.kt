package com.webscare.numberplategenerator.di

import com.webscare.numberplategenerator.data.local.font.FontStorage
import com.webscare.numberplategenerator.ui.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val appModule = module {
    single { FontStorage(androidContext()) }

    viewModel { MainViewModel(get(), get(), get()) }
}