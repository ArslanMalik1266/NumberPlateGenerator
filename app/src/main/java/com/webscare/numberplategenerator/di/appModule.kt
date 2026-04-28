package com.webscare.numberplategenerator.di

import com.webscare.numberplategenerator.ui.MainViewModel
import org.koin.dsl.module
import org.koin.androidx.viewmodel.dsl.viewModel

val appModule = module {

    viewModel { MainViewModel(get(), get()) }
}