package br.com.fbsantos.baseapp.di

val appModule = listOf(
    databaseModule,
    repositoryModule,
    useCaseModule,
    networkModule,
    serviceModule,
    viewModelModule
)