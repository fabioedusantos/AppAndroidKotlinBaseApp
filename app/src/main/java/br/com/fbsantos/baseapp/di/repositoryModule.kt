package br.com.fbsantos.baseapp.di

import br.com.fbsantos.baseapp.data.database.AppDatabase
import br.com.fbsantos.baseapp.data.repository.ConfiguracoesRepositoryImpl
import br.com.fbsantos.baseapp.domain.repository.ConfiguracoesRepository
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    single {
        ConfiguracoesRepositoryImpl(get<AppDatabase>().configuracoesDao())
    } bind ConfiguracoesRepository::class
}